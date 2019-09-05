using MachineWatcher.Model;
using MachineWatcher.Net;
using MachineWatcher.Net.Api;
using MachineWatcher.Util;
using System;
using System.Diagnostics;
using System.IO;
using System.Runtime.InteropServices;
using System.ServiceProcess;

namespace MachineWatcher
{
	public partial class MachineWatcherService : ServiceBase
	{
		private static readonly string EVENT_LOG_SOURCE = "EventLogSource";
		private static readonly string LOG_NAME = "MachineWatcherLog"; 

		private RestClient restClient;
		private SelfHostApi selfHostApi;
		private string url;
		private int listenPort;

		public MachineWatcherService()
		{
			InitializeComponent();
			SetEventLog();
			LoadPropertiesFile();

			restClient = new RestClient(eventLog, url, listenPort);
			selfHostApi = new SelfHostApi(eventLog,listenPort);
		}

		//plataform windows call method
		[DllImport("advapi32.dll", SetLastError = true)]
		private static extern bool SetServiceStatus(System.IntPtr handle, ref ServiceStatus serviceStatus);

		//WARN: Never block this method
		protected override void OnStart(string[] args)
		{
			eventLog.WriteEntry("In OnStart.");
			ServiceStatus serviceStatus = new ServiceStatus();
			serviceStatus.dwCurrentState = ServiceState.SERVICE_START_PENDING;
			serviceStatus.dwWaitHint = 100000;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service start pending.");

			//start threads
			restClient.Start();
			selfHostApi.Start();

			serviceStatus.dwCurrentState = ServiceState.SERVICE_RUNNING;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service running.");
		}

		protected override void OnStop()
		{
			eventLog.WriteEntry("In OnStop.");
			ServiceStatus serviceStatus = new ServiceStatus();
			serviceStatus.dwCurrentState = ServiceState.SERVICE_STOP_PENDING;
			serviceStatus.dwWaitHint = 100000;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service stop pending.");

			restClient.Interrupt();
			selfHostApi.UnblockAsyncEvent();
			selfHostApi.Interrupt();

			serviceStatus.dwCurrentState = ServiceState.SERVICE_STOPPED;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service stopped.");
		}

		protected override void OnContinue()
		{
			eventLog.WriteEntry("In OnContinue.");
		}

		private void SetEventLog()
		{
			eventLog = new EventLog();
			if (!EventLog.SourceExists(EVENT_LOG_SOURCE))
			{
				EventLog.CreateEventSource(
					EVENT_LOG_SOURCE, LOG_NAME);
			}
			eventLog.Source = EVENT_LOG_SOURCE;
			eventLog.Log = LOG_NAME;
		}

		private void LoadPropertiesFile()
		{
			try
			{
				eventLog.WriteEntry("Loading Properties File config.ini", EventLogEntryType.Information);

				Properties properties = new Properties();
				properties.Load(Environment.ExpandEnvironmentVariables("%ProgramW6432%") + "\\MachineWatcher\\" + "config.ini");
				if (properties.Data.ContainsKey("manager.address") && properties.Data.ContainsKey("manager.port") && properties.Data.ContainsKey("machine.watcher.listen.port"))
				{
					url = "http://" + properties.Data["manager.address"] + ":" + properties.Data["manager.port"] + "/manager/v1/client/win";
					eventLog.WriteEntry("Web Server URL: " + this.url, EventLogEntryType.Information);

					listenPort = Int32.Parse(properties.Data["machine.watcher.listen.port"]);
					eventLog.WriteEntry("MachineWatcher listen on port: " + this.listenPort, EventLogEntryType.Information);
				}
				else
				{
					throw new FileLoadException("File 'config.ini' corrupted check entries 'manager.address', 'manager.port' and 'machine.watcher.listen.port'");
				}
			}
			catch(IOException e)
			{
				eventLog.WriteEntry(e.Message, EventLogEntryType.Error);
				eventLog.WriteEntry(e.StackTrace, EventLogEntryType.Error);
				eventLog.WriteEntry(e.Source, EventLogEntryType.Error);

				throw e;
			}
		}
	}
}

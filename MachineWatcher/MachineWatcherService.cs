using MachineWatcher.Model;
using MachineWatcher.Net;
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
		//cuidado com tamanho dessa string que o sistema para de funcionar se for maior que isso!!!!! WTF!!!!
		private static readonly string LOG_NAME = "MachineWatcherLog"; 

		private RestClient restClient;
		private string url;
		private int listenPort;

		public MachineWatcherService()
		{
			InitializeComponent();
			SetEventLog();
			LoadPropertiesFile();

			restClient = new RestClient(eventLog, url, listenPort);
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

			/*
			Timer timer = new Timer();
			timer.Interval = 10 * 1000; // 10s
			timer.Elapsed += new ElapsedEventHandler(this.OnTimer);
			timer.Start();
			*/

			//start threads or timers
			restClient.Start();
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

			//TODO: made others processes
			restClient.Interrupt();

			serviceStatus.dwCurrentState = ServiceState.SERVICE_STOPPED;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service stopped.");
		}

		protected override void OnContinue()
		{
			eventLog.WriteEntry("In OnContinue.");
		}

		/*
		private void OnTimer(object sender, ElapsedEventArgs args)
		{
			// TODO: Insert monitoring activities here.
			eventLog.WriteEntry("Monitoring the System", EventLogEntryType.Information, eventId++);
		}
		*/

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
				this.eventLog.WriteEntry("Loading Properties File config.ini", EventLogEntryType.Information);

				Properties properties = new Properties();
				properties.Load(Environment.ExpandEnvironmentVariables("%ProgramW6432%") + "\\MachineWatcher\\" + "config.ini");
				if (properties.Data.ContainsKey("manager.address") && properties.Data.ContainsKey("manager.port") && properties.Data.ContainsKey("machine.watcher.listen.port"))
				{
					this.url = "http://" + properties.Data["manager.address"] + ":" + properties.Data["manager.port"] + "/manager/v1/client/win";
					this.eventLog.WriteEntry("Web Server URL: " + this.url, EventLogEntryType.Information);

					this.listenPort = Int32.Parse(properties.Data["machine.watcher.listen.port"]);
					this.eventLog.WriteEntry("MachineWatcher listen on port: " + this.listenPort, EventLogEntryType.Information);
				}
				else
				{
					throw new FileLoadException("File 'config.ini' corrupted check entries 'manager.address', 'manager.port' and 'machine.watcher.listen.port'");
				}
			}
			catch(IOException e)
			{
				this.eventLog.WriteEntry(e.Message, EventLogEntryType.Error);
				this.eventLog.WriteEntry(e.StackTrace, EventLogEntryType.Error);
				this.eventLog.WriteEntry(e.Source, EventLogEntryType.Error);

				throw e;
			}
		}
	}
}

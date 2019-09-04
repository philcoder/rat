using MachineWatcher.Model;
using System.Diagnostics;
using System.Runtime.InteropServices;
using System.ServiceProcess;
using System.Timers;

namespace MachineWatcher
{
	public partial class MachineWatcherService : ServiceBase
	{
		private static readonly string EVENT_LOG_SOURCE = "EventLogSource";
		//cuidado com tamanho dessa string que o sistema para de funcionar se for maior que isso!!!!! WTF!!!!
		private static readonly string LOG_NAME = "MachineWatcherLog"; 

		private int eventId = 1;

		public MachineWatcherService()
		{
			InitializeComponent();

			eventLog = new System.Diagnostics.EventLog();
			if (!System.Diagnostics.EventLog.SourceExists(EVENT_LOG_SOURCE))
			{
				System.Diagnostics.EventLog.CreateEventSource(
					EVENT_LOG_SOURCE, LOG_NAME);
			}
			eventLog.Source = EVENT_LOG_SOURCE;
			eventLog.Log = LOG_NAME;
		}

		protected override void OnStart(string[] args)
		{
			eventLog.WriteEntry("In OnStart.");
			ServiceStatus serviceStatus = new ServiceStatus();
			serviceStatus.dwCurrentState = ServiceState.SERVICE_START_PENDING;
			serviceStatus.dwWaitHint = 100000;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service start pending.");

			Timer timer = new Timer();
			timer.Interval = 10 * 1000; // 10s
			timer.Elapsed += new ElapsedEventHandler(this.OnTimer);
			timer.Start();

			//TODO: made others processes


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

			serviceStatus.dwCurrentState = ServiceState.SERVICE_STOPPED;
			SetServiceStatus(this.ServiceHandle, ref serviceStatus);
			eventLog.WriteEntry("Service stopped.");
		}

		protected override void OnContinue()
		{
			eventLog.WriteEntry("In OnContinue.");
		}

		//plataform windows call method
		[DllImport("advapi32.dll", SetLastError = true)]
		private static extern bool SetServiceStatus(System.IntPtr handle, ref ServiceStatus serviceStatus);

		private void OnTimer(object sender, ElapsedEventArgs args)
		{
			// TODO: Insert monitoring activities here.
			eventLog.WriteEntry("Monitoring the System", EventLogEntryType.Information, eventId++);
		}
	}
}

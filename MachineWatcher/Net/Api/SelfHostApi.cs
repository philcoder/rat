using MachineWatcher.Util;
using System;
using System.Diagnostics;
using System.Threading;
using System.Web.Http;
using System.Web.Http.SelfHost;

namespace MachineWatcher.Net.Api
{
	public class SelfHostApi : Runnable
	{
		private EventLog eventLog;
		private int listenPort;
		private string ip;

		private ManualResetEvent shutDown = new ManualResetEvent(false);

		public SelfHostApi(EventLog eventLog,int listenPort)
		{
			this.eventLog = eventLog;
			this.listenPort = listenPort;
			ip = new CollectInfo(listenPort).GetNetInfo().Ip;
		}

		protected override void Run()
		{
			try
			{
				eventLog.WriteEntry("Start SelfHostApi Thread", EventLogEntryType.Information);
				var baseAddress = "http://" + ip + ":" + listenPort + "/";
				//var baseAddress = "http://localhost:" + listenPort + "/";
				var config = new HttpSelfHostConfiguration(baseAddress);
				config.Routes.MapHttpRoute(
					name: "MachineWatcherApi",
					routeTemplate: "watcher/v1/{controller}/{id}",
					defaults: new { id = RouteParameter.Optional }
				);

				using (HttpSelfHostServer server = new HttpSelfHostServer(config))
				{
					server.OpenAsync().Wait();
					shutDown.WaitOne();//wait signal to block unblock async event
				}
			}
			catch (Exception e)
			{
				eventLog.WriteEntry("The SelfHostApi with problems", EventLogEntryType.Warning);
				eventLog.WriteEntry(e.Message, EventLogEntryType.Error);
				eventLog.WriteEntry(e.StackTrace, EventLogEntryType.Error);
				eventLog.WriteEntry(e.Source, EventLogEntryType.Error);
			}
		}

		public void UnblockAsyncEvent()
		{
			eventLog.WriteEntry("The SelfHostApi UnblockAsyncEvent for shutdown thread safe", EventLogEntryType.Warning);
			shutDown.Set();
		}
	}
}

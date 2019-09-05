
using MachineWatcher.Model;
using MachineWatcher.Util;
using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Threading;

namespace MachineWatcher.Net
{
	public class RestClient : Runnable
	{
		private readonly string contentType = "application/json; charset=utf-8";

		private State state;
		private string url;
		private WebRequest webRequest;
		private JsonSerializer serializer;

		private EventLog eventLog;
		private int eventId = 1;

		public RestClient(EventLog eventLog, string url)
		{
			this.eventLog = eventLog;
			this.url = url;

			this.state = State.REGISTER;
			this.serializer = new JsonSerializer();
		}
		protected override void Run()
		{
			try
			{
				Machine machine = new CollectInfoMachine().Data;
				while (running)
				{
					this.eventLog.WriteEntry("Monitoring the System from Thread", EventLogEntryType.Information, eventId++);

					machine.Port = eventId;
					this.eventLog.WriteEntry("Machine Data: " + this.serializer.Serialize(machine), EventLogEntryType.Information, eventId++);
					Thread.Sleep(10000);
				}
			}
			catch (Exception e)
			{
				this.eventLog.WriteEntry(e.Message, EventLogEntryType.Error);
				this.eventLog.WriteEntry(e.StackTrace, EventLogEntryType.Error);
				this.eventLog.WriteEntry(e.Source, EventLogEntryType.Error);
			}
		}

		private void SetWebRequest(string operacao, string metodo)
		{
			webRequest = WebRequest.Create(url + "/" + operacao);
			webRequest.Method = metodo;
			webRequest.ContentType = this.contentType;
		}
	}

	
}

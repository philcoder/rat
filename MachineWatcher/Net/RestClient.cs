﻿
using MachineWatcher.Model;
using MachineWatcher.Util;
using System.Diagnostics;
using System.Net;
using System.Threading;

namespace MachineWatcher.Net
{
	public class RestClient : Runnable
	{
		private readonly string contentType = "application/json; charset=utf-8";

		private State state;
		private string url = "http://localhost:61263/ServiceUsuario.svc"; //depois vai para um arquivo de configuracao ou equivalente isso
		private WebRequest webRequest;
		private JsonSerializer serializer;

		private EventLog eventLog;
		private int eventId = 1;

		public RestClient(EventLog eventLog)
		{
			this.eventLog = eventLog;
			this.state = State.REGISTER;
			this.serializer = new JsonSerializer();
		}
		protected override void Run()
		{
			Machine machine = new CollectInfoMachine().Data;
			while (running)
			{
				this.eventLog.WriteEntry("Monitoring the System from Thread", EventLogEntryType.Information, eventId++);

				machine.Port = eventId;
				this.eventLog.WriteEntry("Machine Data: "+this.serializer.Serialize(machine), EventLogEntryType.Information, eventId++);
				Thread.Sleep(10000);
			}
		}

		private void PreparaWebRequest(string operacao, string metodo)
		{
			webRequest = WebRequest.Create(url + "/" + operacao);
			webRequest.Method = metodo;
			webRequest.ContentType = this.contentType;
		}
	}

	
}

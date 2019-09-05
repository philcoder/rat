
using MachineWatcher.Model;
using MachineWatcher.Model.Response;
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

		private JsonSerializer serializer;
		private CollectInfo collectInfo;

		private EventLog eventLog;
		private int eventId = 1;
		private int serverClientId;

		public RestClient(EventLog eventLog, string url, int listenPort)
		{
			this.eventLog = eventLog;
			this.url = url;

			this.state = State.REGISTER;
			this.serializer = new JsonSerializer();
			this.collectInfo = new CollectInfo(listenPort);
		}
		protected override void Run()
		{
			eventLog.WriteEntry("Monitoring the System from Thread", EventLogEntryType.Information, eventId++);

			while (running)
			{
				try
				{
					if (state == State.REGISTER)
					{
						if (SentRegister(collectInfo.GetMachineData()))
						{
							state = State.HEARTBEAT;
							eventLog.WriteEntry("Now Only sent heartbeats to id: " + serverClientId, EventLogEntryType.Information, eventId++);
						}
						else
						{
							Thread.Sleep(10000);
						}
					}
					else
					{
						NetworkInfo netInfo = collectInfo.GetNetInfo();
						if (SendHeartbeat(netInfo))
						{
							Thread.Sleep(20000);
						}
						else
						{
							state = State.REGISTER;
						}
					}
				}
				catch (Exception e)
				{
					state = State.REGISTER;
					Thread.Sleep(10000);

					eventLog.WriteEntry("Try again to registry!", EventLogEntryType.Warning, eventId++);
					eventLog.WriteEntry(e.Message, EventLogEntryType.Error);
					eventLog.WriteEntry(e.StackTrace, EventLogEntryType.Error);
					eventLog.WriteEntry(e.Source, EventLogEntryType.Error);
				}
			}
		}


		private void Sent(Stream stream, string json)
		{
			using (var streamWriter = new StreamWriter(stream))
			{
				streamWriter.Write(json);
				streamWriter.Flush();
				streamWriter.Close();
			}
		}


		private bool SentRegister(Machine machine)
		{
			WebRequest webRequest = GetWebRequest("register");
			string json = serializer.Serialize(machine);
			eventLog.WriteEntry("SentRegister Data: " + json, EventLogEntryType.Information, eventId++);

			Sent(webRequest.GetRequestStream(), json);
			return ResponseRegister((HttpWebResponse)webRequest.GetResponse());
		}

		private bool SendHeartbeat(NetworkInfo netInfo)
		{
			WebRequest webRequest = GetWebRequest("heartbeat/" + serverClientId, "PUT");
			string json = serializer.Serialize(netInfo);
			//eventLog.WriteEntry(json, EventLogEntryType.Information, eventId++);

			Sent(webRequest.GetRequestStream(), json);
			return ResponseHeartbeat((HttpWebResponse)webRequest.GetResponse());
		}

		private string Response(Stream stream)
		{
			string json;
			using (var streamReader = new System.IO.StreamReader(stream))
			{
				json = streamReader.ReadToEnd();
			}
			return json;
		}

		private bool ResponseRegister(HttpWebResponse httpWebResponse)
		{
			string json = Response(httpWebResponse.GetResponseStream());
			if (json != null && httpWebResponse.StatusCode == HttpStatusCode.OK)
			{
				MachineMessage message = serializer.Deserialize<MachineMessage>(json);
				serverClientId = message.Id;
				return true;
			}

			return false;
		}

		private bool ResponseHeartbeat(HttpWebResponse httpWebResponse)
		{
			string json = Response(httpWebResponse.GetResponseStream());
			if (json != null && httpWebResponse.StatusCode == HttpStatusCode.OK)
			{
				ResponseMessage message = serializer.Deserialize<ResponseMessage>(json);
				return message.Message.Contains("ok");
			}

			return false;
		}

		private WebRequest GetWebRequest(string operacao, string metodo = "POST")
		{
			WebRequest webRequest = WebRequest.Create(url + "/" + operacao);
			webRequest.Method = metodo;
			webRequest.ContentType = contentType;

			return webRequest;
		}
	}


}

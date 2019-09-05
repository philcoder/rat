using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MachineWatcher.Net.Api
{	
	//ROTE: watcher/v1/{name_controller}/{id} this name_controller is 'terminal'
	public class TerminalController : ApiController
	{
		// GET watcher/v1/terminal
		public HttpResponseMessage Get()
		{
			var hello = new string[] { "hello", "world", "from", "windows", "service", DateTime.Now.ToString() };
			return Request.CreateResponse(HttpStatusCode.OK, hello, Configuration.Formatters.JsonFormatter);
		}

		// POST watcher/v1/terminal
		public HttpResponseMessage Post([FromBody] string payload)
		{
			List<string> response = new List<string>(3);
			response.Add("try remove files");
			response.Add("java -version = 8.0");
			response.Add("test");
			response.Add(payload);

			return Request.CreateResponse(HttpStatusCode.OK, response, Configuration.Formatters.JsonFormatter);
		}
	}
}

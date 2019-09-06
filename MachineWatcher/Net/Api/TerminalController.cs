using MachineWatcher.Model.Message;
using MachineWatcher.Model.Response;
using MachineWatcher.Util;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace MachineWatcher.Net.Api
{	
	public class TerminalController : ApiController
	{
		private Terminal terminal;

		public TerminalController()
		{
			terminal = new Terminal();
		}
		
		// POST watcher/v1/terminal
		[HttpPost]
		public HttpResponseMessage ExecuteCommands(CommandMessage cmd)
		{
			if(cmd.Commands == null || cmd.Commands.Equals(""))
			{
				ResponseMessage response = new ResponseMessage();
				response.Message = "The command passed was null or blank";
				return Request.CreateResponse(HttpStatusCode.BadRequest, response, Configuration.Formatters.JsonFormatter);
			}

			CommandOutputMessage cmdOutput = new CommandOutputMessage();
			cmdOutput.Outputs = terminal.Execute(cmd.Commands.Trim());
			return Request.CreateResponse(HttpStatusCode.OK, cmdOutput, Configuration.Formatters.JsonFormatter);
		}
	}
}

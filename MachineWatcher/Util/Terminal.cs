using System.Collections.Generic;
using System.Diagnostics;
using System.Text;

namespace MachineWatcher.Util
{
	public class Terminal
	{
		private ProcessStartInfo processStartInfo;

		public Terminal()
		{
			processStartInfo = new ProcessStartInfo("cmd");
			processStartInfo.RedirectStandardInput = true;
			processStartInfo.RedirectStandardOutput = true;
			processStartInfo.RedirectStandardError = true;
			processStartInfo.UseShellExecute = false;
			processStartInfo.StandardOutputEncoding = Encoding.GetEncoding(850);
			processStartInfo.StandardErrorEncoding = Encoding.GetEncoding(850);
		}

		public List<string> Execute(string cmd)
		{
			List<string> lines = new List<string>();

			processStartInfo.Arguments = "/c " + cmd;
			Process process = Process.Start(processStartInfo);
			if (process != null)
			{
				string line;
				while ((line = process.StandardOutput.ReadLine()) != null)
				{
					lines.Add(ConvertStringEncode(line));
				}
				process.StandardOutput.Close();

				while ((line = process.StandardError.ReadLine()) != null)
				{
					lines.Add(ConvertStringEncode(line));
				}
				process.StandardError.Close();

				process.Close();
			}

			return lines;
		}

		public string GetStringFromExecute(string cmd)
		{
			StringBuilder output = new StringBuilder();
			List<string> lines = Execute(cmd);
			foreach(var line in lines)
			{
				output.Append(line);
			}
			return output.ToString();
		}

		private string ConvertStringEncode(string value)
		{
			Encoding terminalEncoding = Encoding.GetEncoding(850);
			Encoding utf8 = Encoding.UTF8;
			var terminalBytes = terminalEncoding.GetBytes(value);
			byte[] utf8Bytes = Encoding.Convert(terminalEncoding, utf8, terminalBytes);
			return utf8.GetString(utf8Bytes);
		}
	}
}

using MachineWatcher.Model;
using System;
using System.Collections.Generic;
using System.Net;
using System.Linq;
using System.Net.Sockets;

namespace MachineWatcher.Util
{
	//extract data from running machine...
	public class CollectInfoMachine
	{
		private Machine _machine;

		public Machine Data
		{
			get
			{
				return _machine;
			}
		}

		public CollectInfoMachine()
		{
			this._machine = new Machine();

			ExtractData();
		}

		private void ExtractData()
		{
			this._machine.Name = Environment.MachineName;
			this._machine.DotNetVersion = Environment.Version.ToString();
			this._machine.WindowsVersion = Environment.OSVersion.VersionString;
			this._machine.Ip = GetIp();

			//TODO: disk available and total
			//TODO: get firewall active and anti-virus active
		}

		private string GetIp()
		{
			var localIp = IPAddress.None;//255.255.255.255
			try
			{
				//use socket udp for get ip endpoint
				using (var socket = new Socket(AddressFamily.InterNetwork, SocketType.Dgram, 0))
				{
					// Try to connect to Google's Public DNS service
					socket.Connect("8.8.8.8", 65530);
					if (!(socket.LocalEndPoint is IPEndPoint endPoint))
					{
						throw new InvalidOperationException($"Error occurred casting {socket.LocalEndPoint} to IPEndPoint");
					}
					localIp = endPoint.Address;
				}
			}
			catch (SocketException ex)
			{
			}

			return localIp.ToString();
		}
	}
}

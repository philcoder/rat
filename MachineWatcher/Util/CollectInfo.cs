using MachineWatcher.Model;
using System;
using System.Net;
using System.Net.Sockets;
using System.Management;

namespace MachineWatcher.Util
{
	//extract data from running machine...
	public class CollectInfo
	{

		private int listenPort;

		public CollectInfo(int listenPort)
		{
			this.listenPort = listenPort;
		}

		public Machine GetMachineData()
		{
			var machine = new Machine();
			machine.Hostname = Environment.MachineName;
			machine.DotNetVersion = Environment.Version.ToString();
			machine.WindowsVersion = GetOSFriendlyName();
			machine.Ip = GetIp();
			machine.Port = listenPort;

			//TODO: disk available and total
			//TODO: get firewall active and anti-virus active

			return machine;
		}

		public NetworkInfo GetNetInfo()
		{
			var netInfo = new NetworkInfo();
			netInfo.Hostname = Environment.MachineName;
			netInfo.Ip = GetIp();
			netInfo.Port = listenPort;
			return netInfo;
		}

		private string GetOSFriendlyName()
		{
			ManagementObjectSearcher searcher = new ManagementObjectSearcher("SELECT Caption FROM Win32_OperatingSystem");
			foreach (ManagementObject os in searcher.Get())
			{
				return os["Caption"].ToString();
			}
			return Environment.OSVersion.VersionString; //version with version and etc
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

using MachineWatcher.Model;
using System;
using System.Net;
using System.Net.Sockets;
using System.Management;
using System.Diagnostics;
using System.Collections.Generic;
using System.IO;

namespace MachineWatcher.Util
{
	//extract data from running machine...
	public class CollectInfo
	{
		private int listenPort;

		private Terminal terminal;

		public CollectInfo(int listenPort)
		{
			this.listenPort = listenPort;
			terminal = new Terminal();
		}

		public Machine GetMachineData()
		{
			var machine = new Machine();
			machine.Hostname = Environment.MachineName;
			machine.DotNetVersion = Environment.Version.ToString();
			machine.WindowsVersion = GetOSFriendlyName();
			machine.Ip = GetIp();
			machine.Port = listenPort;
			machine.Firewall = GetFirewallStatus();
			machine.Antivirus = GetAntivirusInstalled();
			machine.Drives = GetDrivesInstalled();

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
			catch (SocketException e)
			{
				throw e;
			}

			return localIp.ToString();
		}

		private bool GetFirewallStatus()
		{
			string output = terminal.GetStringFromExecute("netsh advfirewall show allprofiles state");
			return output.Contains("Ligado") || output.Contains("ON");
		}

		private bool GetAntivirusInstalled()
		{
			try
			{
				string wmipathstr = @"\\" + Environment.MachineName + @"\root\SecurityCenter2";
				ManagementObjectSearcher searcher = new ManagementObjectSearcher(wmipathstr, "SELECT * FROM AntivirusProduct");
				ManagementObjectCollection instances = searcher.Get();
				return instances.Count > 0;
			}
			catch (Exception e)
			{
				throw e;
			}
		}

		private List<Drive> GetDrivesInstalled()
		{
			List<Drive> drives = new List<Drive>(3);
			DriveInfo[] allDrives = DriveInfo.GetDrives();
			foreach (DriveInfo driveInfo in allDrives)
			{
				Drive drive = new Drive();
				drive.Name = driveInfo.Name;

				if (driveInfo.IsReady == true)
				{
					drive.AvailableSpace = driveInfo.TotalFreeSpace;
					drive.TotalSpace = driveInfo.TotalSize;
				}

				drives.Add(drive);
			}

			return drives;
		}
	}
}

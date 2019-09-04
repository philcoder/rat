namespace MachineWatcher.Model
{
	public class Machine
	{
		public string Name { get; set; }
		public string Ip { get; set; }
		public int Port { get; set; }
		public string WindowsVersion { get; set; }
		public string DotNetVersion { get; set; }
		public string DiskAvailable { get; set; }
		public string DiskTotal { get; set; }
		public bool Antivirus { get; set; }
		public bool Firewall { get; set; }
	}
}

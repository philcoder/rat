using System.Collections.Generic;
using System.Runtime.Serialization;

namespace MachineWatcher.Model
{
	[DataContract]
	public class Machine
	{
		public Machine()
		{
			Drives = new List<Drive>();
		}

		[DataMember(Name = "hostname")]
		public string Hostname { get; set; }
		[DataMember(Name = "ip")]
		public string Ip { get; set; }
		[DataMember(Name = "port")]
		public int Port { get; set; }
		[DataMember(Name = "drives")]
		public List<Drive> Drives { get; set; }
		[DataMember(Name = "windowsVersion")]
		public string WindowsVersion { get; set; }
		[DataMember(Name = "dotNetVersion")]
		public string DotNetVersion { get; set; }
		[DataMember(Name = "antivirus")]
		public bool Antivirus { get; set; }
		[DataMember(Name = "firewall")]
		public bool Firewall { get; set; }
	}
}

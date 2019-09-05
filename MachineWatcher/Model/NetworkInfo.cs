using System.Runtime.Serialization;

namespace MachineWatcher.Model
{
	[DataContract]
	public class NetworkInfo
	{
		[DataMember(Name = "hostname")]
		public string Hostname { get; set; }
		[DataMember(Name = "ip")]
		public string Ip { get; set; }
		[DataMember(Name = "port")]
		public int Port { get; set; }
	}
}

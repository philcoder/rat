
using System.Runtime.Serialization;

namespace MachineWatcher.Model
{
	[DataContract]
	public class Drive
	{
		[DataMember(Name = "name")]
		public string Name { get; set; }
		[DataMember(Name = "availableSpace")]
		public long AvailableSpace { get; set; }
		[DataMember(Name = "totalSpace")]
		public long TotalSpace { get; set; }
	}
}

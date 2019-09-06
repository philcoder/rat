
using System.Runtime.Serialization;

namespace MachineWatcher.Model.Message
{
	[DataContract]
	public class CommandMessage
	{
		[DataMember(Name = "commands")]
		public string Commands { get; set; }
	}
}

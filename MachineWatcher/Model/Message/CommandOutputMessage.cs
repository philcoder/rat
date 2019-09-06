using System.Collections.Generic;
using System.Runtime.Serialization;

namespace MachineWatcher.Model.Message
{
	[DataContract]
	public class CommandOutputMessage
	{
		[DataMember(Name = "outputs")]
		public List<string> Outputs { get; set; }
	}
}

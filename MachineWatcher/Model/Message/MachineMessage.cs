using System.Runtime.Serialization;

namespace MachineWatcher.Model.Response
{
	[DataContract]
	public class MachineMessage : ResponseMessage
	{
		[DataMember(Name = "id")]
		public int Id { get; set; }
	}
}

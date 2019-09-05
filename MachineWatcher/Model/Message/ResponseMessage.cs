using System.Runtime.Serialization;

namespace MachineWatcher.Model.Response
{
	[DataContract]
	public class ResponseMessage
	{
		[DataMember(Name = "message")]
		public string Message { get; set; }
	}
}

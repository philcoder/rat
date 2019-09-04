using System;
using System.IO;
using System.Runtime.Serialization.Json;
using System.Text;

namespace MachineWatcher.Net
{
	class JsonSerializer
	{
		//obj to string
		public string Serialize<T>(T obj)
		{
			DataContractJsonSerializer serializer = new DataContractJsonSerializer(obj.GetType());
			MemoryStream memoryStream = new MemoryStream();
			serializer.WriteObject(memoryStream, obj);
			string stringJson = Encoding.UTF8.GetString(memoryStream.ToArray());

			return stringJson;
		}

		//string to obj
		public T Deserialize<T>(string stringJson)
		{
			T obj = Activator.CreateInstance<T>();
			MemoryStream memoryStream = new MemoryStream(Encoding.Unicode.GetBytes(stringJson));
			DataContractJsonSerializer serializer = new DataContractJsonSerializer(obj.GetType());
			obj = (T)serializer.ReadObject(memoryStream);
			memoryStream.Close();
			return obj;
		}
	}
}

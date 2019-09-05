using System.Collections.Generic;
using System.IO;

namespace MachineWatcher.Util
{
	public sealed class Properties
	{
		public Dictionary<string, string> Data { get; private set; }

		public Properties()
		{
			Data = new Dictionary<string, string>();
		}

		public void Load(string path)
		{
			foreach (var row in File.ReadAllLines(path))
			{
				string line = row.Trim();
				if (!line.StartsWith("#"))
				{
					string[] itens = line.Split('=');
					if (itens.Length == 2)
					{
						Data.Add(itens[0], itens[1]);
					}
				}
			}
		}
	}
}

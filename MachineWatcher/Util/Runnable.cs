using System.Threading;

namespace MachineWatcher.Util
{
	public abstract class Runnable
	{
		private Thread thread;
		protected volatile bool running = false;

		protected abstract void Run();

		//Start Asynchronous
		public void Start()
		{
			if (thread == null)
			{
				running = true;
				thread = new Thread(new ThreadStart(Run));
				thread.Start();
			}
			else
			{
				throw new ThreadStateException("The thread has already been started.");
			}
		}

		public void Stop()
		{
			running = false;
		}

		public void Join()
		{
			thread.Join();
		}

		public void Join(int timeout)
		{
			thread.Join(timeout);
		}

		public void Interrupt()
		{
			thread.Abort();
		}

		public bool IsAlive()
		{
			return thread.IsAlive;
		}

		public void RunSynchronous()
		{
			Start();
			Join();
		}
	}
}

package cz.kadledav.am.client.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;

class Psapi {
	static {
		Native.register("psapi");
	}

	public static native int GetModuleBaseNameW(Pointer hProcess, Pointer hmodule, char[] lpBaseName, int size);
}
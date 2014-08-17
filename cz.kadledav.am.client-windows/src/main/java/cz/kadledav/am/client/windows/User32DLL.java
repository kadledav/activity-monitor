package cz.kadledav.am.client.windows;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

class User32DLL {
	static {
		Native.register("user32");
	}

	public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);

	public static native HWND GetForegroundWindow();

	public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
}

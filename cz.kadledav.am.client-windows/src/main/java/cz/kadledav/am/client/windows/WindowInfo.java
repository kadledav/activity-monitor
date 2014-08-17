package cz.kadledav.am.client.windows;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

import cz.kadledav.am.client.StateInfo;
import cz.kadledav.am.client.StateInformator;

public class WindowInfo implements StateInformator {
	
	private static final int MAX_TITLE_LENGTH = 1000;
	
	public StateInfo getInfo() {
		char[] buffer = new char[MAX_TITLE_LENGTH * 2];
		HWND foregroundWindow = User32DLL.GetForegroundWindow();
		
		PointerByReference pointer = new PointerByReference();
		User32DLL.GetWindowThreadProcessId(foregroundWindow, pointer);
		Pointer process = Kernel32.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ, false, pointer.getValue());
		Psapi.GetModuleBaseNameW(process, null, buffer, MAX_TITLE_LENGTH);
		String processName = Native.toString(buffer);
		
		User32DLL.GetWindowTextW(foregroundWindow, buffer, MAX_TITLE_LENGTH);
        String title = Native.toString(buffer);
        return new StateInfo(processName, title);
	}
}

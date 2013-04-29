import subprocess
import win32console

ANYCONNECT_BIN = 'c:\\Program Files (x86)\\Cisco\\Cisco AnyConnect Secure Mobility Client\\vpncli.exe'
GROUP = ''
USERNAME = ''
PASSWORD = '.x11.13x'

def write_console_input(text):
  stdin = win32console.GetStdHandle(win32console.STD_INPUT_HANDLE)
  ir = win32console.PyINPUT_RECORDType(win32console.KEY_EVENT)
  ir.KeyDown = True
  for ch in text:
    ir.Char = unicode(ch)
    stdin.WriteConsoleInput([ir])

def main():
  proc = subprocess.Popen([ANYCONNECT_BIN,'connect','VPN'],stdin=subprocess.PIPE)
  proc.stdin.write('%s\n%s\n' % (GROUP, USERNAME))
  write_console_input('%s\n' % PASSWORD)
  print("OUTPUT DONE");
  ret = proc.wait()
  print ret

if __name__ == '__main__':
  main()


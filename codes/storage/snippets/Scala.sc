// 
// Scala Snippets
//

// import $ivy.`com.lihaoyi::os-lib:0.11.3`

def exec(commands: String*): Unit = 
  val res = os.proc(commands).call()
  if res.exitCode == 0 then
    print(res.out.trim())
  else
    print(res.err.trim())

def execForce(commands: String*): Unit =
  try
    exec(commands*)
  catch
    case ex: Exception => println(ex)
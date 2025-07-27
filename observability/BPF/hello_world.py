from bcc import BPF

b = BPF(text="""
int kprobe__sys_clone()
{
  bpf_trace_printk("Hello, World!\\n");
  return 0;
}
""")

b.trace_print()
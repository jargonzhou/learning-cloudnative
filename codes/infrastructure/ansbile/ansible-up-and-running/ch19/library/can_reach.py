""" can_reach ansible module """
from ansible.module_utils.basic import AnsibleModule


def can_reach(module, host, port, timeout):
  """ can_reach is a method that does a tcp connect with nc """
  nc_path = module.get_bin_path('nc', required=True)
  args = [nc_path, "-z", "-w", str(timeout), host, str(port)]
  # (return_code, stdout, stderr) = module.run_command(args)
  return module.run_command(args, check_rc=True)


def main():
  """ ansible module that uses netcat to connect """
  module = AnsibleModule(
      argument_spec=dict(
          host=dict(required=True),
          port=dict(required=True, type='int'),
          timeout=dict(required=False, type='int', default=3)
      ),
      supports_check_mode=True
  )

  # In check mode, we take no action
  # Since this module never changes system state, we just
  # return changed=False
  if module.check_mode:
    module.exit_json(changed=False)

  host = module.params['host']
  port = module.params['port']
  timeout = module.params['timeout']

  if can_reach(module, host, port, timeout)[0] == 0:
    msg = "Could reach %s:%s" % (host, port)
    module.exit_json(changed=False, msg=msg)
  else:
    msg = "Could not reach %s:%s" % (host, port)
    module.fail_json(msg=msg)


if __name__ == "__main__":
  main()

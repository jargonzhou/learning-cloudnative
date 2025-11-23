#include "apue.h"
#include "apue_sock.h"

// see /etc/services
int
main (int argc, char *argv[])
{
  struct servent *serv;
  char **alias;

  while ((serv = getservent ()) != NULL)
    {
      printf ("s_name=%s, s_port=%d, s_proto=%s", serv->s_name, serv->s_port,
              serv->s_proto);
      alias = serv->s_aliases;
      if (*alias != NULL)
        {
          printf ("s_alias=( ");
          for (; *alias != NULL; alias++)
            {
              printf ("%s ", *alias);
            }
          printf (")\n");
        }
      else
        printf ("\n");
    }
  endservent ();

  exit (0);
}
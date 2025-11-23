#include "apue.h"

typedef struct node *NodePtr;
typedef struct node
{
  char *name;
  NodePtr left;
  NodePtr right;
} Node;

int
main (int argc, char *argv[])
{
  struct node *np;
  np = (NodePtr)malloc (sizeof (Node));
  if (np == NULL)
    {
      err_sys ("malloc failed");
    }

  np->name = "hello";
  printf ("%p\n", np);

  free (np);
  return 0;
}

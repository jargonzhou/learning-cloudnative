#ifndef CH04_PERSON
#define CH04_PERSON

class Person
{
public:
  char LastName[11], FirstName[11], Address[16];
  char City[16], State[10], ZipCode[10];
  Person ();
};

Person::Person ()
{
  LastName[0] = 0;
  FirstName[0] = 0;
  Address[0] = 0;
  City[0] = 0;
  State[0] = 0;
  ZipCode[0] = 0;
}

#endif /* CH04_PERSON */

#ifndef CH01_PERSON
#define CH01_PERSON
#include <spdlog/fmt/ostr.h>

class Person
{
public:
  char LastName[11], FirstName[11], Address[16];
  char City[16], State[3], ZipCode[10];
  Person ();

  // for spdlog
  template <typename OStream>
  friend OStream &
  operator<< (OStream &os, const Person &p)
  {
    return os << "[Person LastName=" << p.LastName
              << ", FirstName=" << p.FirstName << ", Address=" << p.Address
              << ", City=" << p.City << ", State=" << p.State
              << ", ZipCode=" << p.ZipCode << "]";
  }
};

#endif /* CH01_PERSON */

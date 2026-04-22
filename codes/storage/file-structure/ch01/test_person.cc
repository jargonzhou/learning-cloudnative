#include "person.h"
#include <string>

#include "spdlog/spdlog.h"

int
main (int argc, char const *argv[])
{

  spdlog::set_level (spdlog::level::debug);
  spdlog::set_pattern (
      "%Y-%m-%d %H:%M:%S,%e [%l] [process %P thread %t] - %v");
  spdlog::info ("Welcome to spdlog!");

  Person p;
  strcpy (p.FirstName, "hello");
  Person *p_ptr = new Person;
  strcpy (p_ptr->FirstName, "there");
  spdlog::info ("create person: {}", p); // spdlog with domain object

  return 0;
}

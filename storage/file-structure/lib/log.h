#ifndef LIB_LOG
#define LIB_LOG

#define SPDLOG_ACTIVE_LEVEL SPDLOG_LEVEL_DEBUG
#include "spdlog/spdlog.h"

#define DEBUG(...)                                                            \
  SPDLOG_LOGGER_DEBUG (spdlog::default_logger_raw (), __VA_ARGS__)
#define ERROR(...)                                                            \
  SPDLOG_LOGGER_ERROR (spdlog::default_logger_raw (), __VA_ARGS__)

inline bool
init_log ()
{
  spdlog::set_level (spdlog::level::debug);
  spdlog::set_pattern ("%Y-%m-%d %H:%M:%S,%e [%l] [%P %t] [%s %# %!] - %v");
  DEBUG ("spdlog macro has been initialized.");
  spdlog::info ("spdlog has been initialized.");
  return true;
}

#endif /* LIB_LOG */

#ifndef STRING_SPACE_H
#define STRING_SPACE_H

#include <string>
using namespace std;
using namespace std;

/* an extended string space that stores strings without null
 * terminators in an array. This string space manages both
 * starting locations and offsets so that client code must only
 * know the starting location of the string it wants.
 */
// TODO: deal with duplicate entrys
// TODO: don't realloc the whole freakin' thing,
// use the segmenting scheme to deal with many memory blocks

class StringSpace {
 public:
  StringSpace(int segment_size=10);
  ~StringSpace();
  int enter(const char *);
  char * get(int loc, int &length);
  void dump();
  void dump2();
  bool find(const char*, int &loc, int &length);
  void delete_back_to(int i);
 private:
  void _extend_space();
  char *_data;
  char *_tail;
  int _used;
  int _current_size;
  int _segment_size;
  int _top_index;
  int _locs[100];
  int _lengths[100];
};

#endif

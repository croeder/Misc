#include <string>
#include "string.h"
#include <iostream>
#include <iomanip>
using namespace std;
#include "StringSpace.h"

StringSpace::StringSpace(int segment_size) {
  _data = new char (segment_size);
  _tail = _data;
  _used = 0;
  _current_size = segment_size;
  _segment_size = segment_size;
  _top_index = -1;
}

StringSpace::~StringSpace() {
  delete _data;
}

void StringSpace::dump() {
  //cout << "used:" << _used << " " << " _current_size:" <<  _current_size  << " _data:" << reinterpret_cast<int>(_data) << endl;
  cout << "used:" << _used << " " << " _current_size:" <<  _current_size  <<  endl;
  for (int i=0; i< _current_size; i++) {
    //cout << setw(3) << i << " " << *(_data+i) << " " <<  (int) (_data+i) << endl;
    cout << setw(3) << i << " " << *(_data+i) << " " << endl;
  }
}

void StringSpace::dump2() {
  cout << "top index: " << _top_index << " current_size " << _current_size << endl;
  char buffer[100];
  for (int i=0; i<=_top_index; i++ ) {
    cout << "i: " << setw(5) << i; 
    cout << " length: " << setw(10) << _lengths[i];
    cout << " loc:    " << setw(10) << (int) _locs[i] << endl;
    int l;
    char *s = get(i, l);
    strncpy(buffer, s, l);
    buffer[l] = '\0';
    cout <<  "\"" << buffer << "\"" <<  endl;
  }
  cout << endl;
}

void StringSpace::_extend_space() {
  cout << "extending..." << endl;
  char *old = _data;
  int n = _current_size / _segment_size;
  n++;
  int old_size = _current_size;
  _current_size = _segment_size * n;
  _data = new char[_current_size];
  memcpy(_data, old, old_size);
  delete old;
  _tail = _data + _used;	
}

int StringSpace::enter(const char *c_string) {
  int length = strlen(c_string);

  if (_used + length >= _current_size) {
    _extend_space();
  }

  strncpy(_tail, c_string, length); 
  _top_index++;
  _locs[_top_index] = (int) (_tail - _data);
  _lengths[_top_index] = length;

  _used += length;
  _tail += length;

  return _top_index;
}

char * StringSpace::get(int index, int &length) {
  length = _lengths[index];
  return _data + _locs[index] ;
}


void StringSpace::delete_back_to(int index) {
  /* delete space back to and including the location passed in */
  /* loc is an offset in bytes into the data area */

  for (int i=_top_index; i>=index; i--) {
    _tail -= _lengths[i];
  }
  _top_index = index;
}
  

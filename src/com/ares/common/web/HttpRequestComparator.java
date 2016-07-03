package com.ares.common.web;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/*
  */


/**
 * Sorts files by sequence (directories before files).
 *
 * @author Michael Morar
 */
public class HttpRequestComparator implements Comparator<HttpRequest> {
  

  public int compare(HttpRequest h1, HttpRequest h2) {
    try {

      if (h1.getSequence() < h2.getSequence()) {
        return -1;
      } else if (h1.getSequence() == h2.getSequence()) {
        return 0;
      } else if (h1.getSequence() > h2.getSequence()) {
        return 1; 
      }
    } catch (ClassCastException ex) {}

    return 0;
  }

}
package com.github.wonderbird.RenameProject.Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration
{
   private static Configuration instance;

   private Set<RenameFromToPair> fromToPairs = new HashSet<>();

   /**
    * Size of a block read at once from files (in bytes).
    */
   private int readBufferSize;

   private String startDir;

   protected Configuration()
   {
      reset();
   }

   public void addFromToPair(final String aFrom, final String aTo)
   {
      final RenameFromToPair pair = new RenameFromToPair(aFrom, aTo);
      fromToPairs.add(pair);
   }

   public List<RenameFromToPair> getFromToPairs()
   {
      return new ArrayList<>(fromToPairs);
   }

   public int getReadBufferSize()
   {
      return readBufferSize;
   }

   public void setReadBufferSize(final int readBufferSize)
   {
      this.readBufferSize = readBufferSize;
   }

   public String getStartDir()
   {
      return startDir;
   }

   public void setStartDir(final String aStartDir)
   {
      startDir = aStartDir;
   }

   public void reset()
   {
      readBufferSize = 1024 * 1024;
      fromToPairs = new HashSet<>();
   }

   public static Configuration getConfiguration()
   {
      if(instance == null)
      {
         instance = new Configuration();
      }
      return instance;
   }

}

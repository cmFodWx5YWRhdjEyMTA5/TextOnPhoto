package com.dev.signatureonphoto.data.model;

import java.io.Serializable;

public class ItemCross implements Serializable {
   public String name_app;
   public String description_app;
   public String package_name;

   public ItemCross(String name_app, String description_app, String package_name) {
      this.name_app = name_app;
      this.description_app = description_app;
      this.package_name = package_name;
   }
}

package com.itgc.foodsafety.utils;

/**
 * Created by root on 29/10/15.
 */
public class Vars {
    public static final String Version = "V2.0";

//    public static final String BASE_URL = "http://audit.foodsafetyhelpline.com/api/";  // live base URL OLDER // TODO: 14/12/16  

    //public static final String BASE_URL = "http://foodsafty.msosa.com/api/";

    //public static final String BASE_URL = "http://foodsafty.msosa.com/v3/api/";
    //public static final String BASE_URL = "http://35.154.66.13/v3/api/";// TODO: 6/1/17

    //public static final String BASE_URL_V4 = "http://35.154.96.253/api/"; //v4

    public static final String BASE_URL_V2 = "http://13.126.7.45/api/";
    //public static final String BASE_URL_V3 = "http://13.126.51.105/api/";
    //http://13.126.249.150


    public static final String  STAGING= "http://13.126.249.150/api/";
  //  public static final String  LIVE= "http://52.66.121.97:83/RestServiceImpl.svc/";
    public static final String  LIVE= "http://ylims.com:83/RestServiceImpl.svc/";

    //13.126.179.210

    public static final String BASE_URL = LIVE;

    public static final String AUDIT = "Api/startAudit";
    public static final String REPORT = "Api/report";
    public static final String LOGIN = "Api/login";
    public static final String FORGOT_PASSWORD = "forgotPassword";
    public static final String SUBMIT_REPORT = "submit_report";
    public static final String SUBMIT_REPORT_TEST="submit_reportTest";
    public static final String FEEDBACK = "contact";
    public static final String CONTACT = "contact";
    public static final String STORE_INFO = "store_information";
    public static final String UPDATE_PROFILE = "Api/updateProfile";
    public static final String CHANGE_PASSWORD = "Passwordchange";
    public static final String OFFLINELOGIN = "login";







    public static final String[] remarks = new String[]{"No Exceptions Found.", "Not Applicable.",
            "FSSAI License was not displayed at the prominent place; it was displayed in the back room.",
            "No pallets being used and the food stock was touching the walls in the Grocery & Back Room sections.",
            "There was Flaking/Peeling of walls observed.", "The sliding window panel of the display chiller was dirty.",
            "Gaskets of the chiller were not properly cleaned.", "The entrance door of the meat shop was dirty.",
            "The Gaskets of the chiller and freezer were dirty.",
            "No records for cleaning of chillers & freezers were being maintained.",
            "Air cutter was not in operation. Fly catcher was installed at a height of above 6ft.",
            "Flaking/Peeling of paint found on one of the walls close to the display chiller in the Meat shop.",
            "Gaskets of the chillers in the Meat shop were dirty.",
            "Flycatcher was installed at above 7ft. height in the Meat shop.",
            "Flycatcher was not working in the Back room and was installed at above 7ft. height.",
            "The iron frames for keeping vegetable baskets were not properly cleaned.",
            "Gaskets were not properly cleaned in two chillers in the Grocery.",
            "Vegetable basket frames were dirty in Fruits & Vegetable section.",
            "No Approved Chemicals were found in the store.",
            "Gaskets and Doors were found dirty both in chiller and freezer in the Meat shop.",
            "No sanitizer available and only floor cleaning chemical was found in the store.",
            "Dirty basket and dirty Mop was placed in the meat shop.",
            "No pallets were being used and the food stock was touching the walls in the Back room.",
            "Flycatcher was not in working condition both in the Meat Shop & Grocery section.",
            "Gaskets of the Chillers & Freezers were not properly cleaned in the Grocery section.",
            "The cooling fans of the chillers were dirty in the Grocery section.",
            "Only one approved chemical was found in the store.",
            "Only one UV tube was working in the flycatcher both in the Back room & FnV sections.",
            "Flycatchers were installed at a above 6ft. height in the store.",
            "Hand Wash Sinks were dirty in the meat shop.", "The floor was found to be dirty in the meat Shop.",
            "Floors were not properly cleaned in the Grocery, some food material and cart board were lying on the floor.",
            "The freezer in the meat shop was found to be dirty both from inside and outside.",
            "No Approved Chemicals were found in the Store.", "Flies were found in the Meat Section.",
            "Air cutter of the Meat section was not in operation.",
            "Iron Frames in the Fruits & Vegetable section were not properly cleaned.",
            "One of the Chillers’ cooling fan was dirty in the Grocery Section.",
            "Food Handlers were using Dettol Soap for washing hands in the store.",
            "The chillers' fan covers & Gaskets were dirty.", "No Approved Chemicals were found in the store.",
            "The food shelves were touching the walls.",
            "No pallets were being used and the food stock was directly placed on the floor and also touching the walls.",
            "Fly catcher was installed at a height of above 7ft and only one UV tube was working.",
            "Food Handlers were using Dettol Soap for washing hands in the store.",
            "The temperature inside the freezer was recorded at -15 Degrees Celsius.",
            "The temperature inside the freezer was recorded at -15 Degrees Celsius.",
            "The Gaskets and the Fan Covers of the chillers were dirty.",
            "Hand marks/stains were observed on the glass doors of the chillers.",
            "No Approved Chemicals were found in the store.",
            "Four packets of Chicken Nuggets were found alongside the Veg Products (Frozen Peas) in the freezer.",
            "No sufficient distance maintained from the walls as food shelves were touching the walls.",
            "Back Room - No pallets were being used and the food stock was touching the walls.",
            "Fly Catcher was installed at a height of above 7ft and only one UV tube was in working condition.",
            "There was rust on the bottom body part of the chillers from inside.",
            "One of the UV tubes in the flycatcher was not working.",
            "The Gaskets and the fan covers of both the chillers were dirty.",
            "The packs of Wheat Floor were directly placed on the floor and were also touching the walls on some places.",
            "No Approved Chemicals were found in the store.", "A dead flying insect was found in one of the chillers.",
            "Floor was dirty; lot of footmarks and the deposition of dirt were observed.",
            "The fan covers of the chillers were dirty.", "The fan covers of both chillers were dirty.",
            "Sufficient distance was not maintained between the Iron racks and the walls.",
            "The merchandizing shelves were attached to the walls.", "The floor of the chiller zone was not properly cleaned.",
            "The merchandizing shelves of the chillers were found to be dirty.",
            "The Gaskets of the freezers were not properly cleaned.", "No Approved Chemicals were available in the store.",
            "Frozen Veg food products like Aloo Tikki, Frozen Peas etc. were placed along with Non-Veg frozen food products in the freezer.",
            "Packs of Basmati Rice were placed directly on the floor.", "Edible oil was placed alongside the detergent.",
            "Food articles were placed directly on the floor and were touching the walls.",
            "Bad Storage Practices; Food & Non-Food Articles were placed together.",
            "Food Stock was directly placed on the floor and was touching the walls.",
            "Both Dairy Chillers were overloaded with Food Stock.", "Freezer was overloaded with Food Stock.",
            "Approx. 100 packs of frozen Veg Food Products were kept in the chillers.",
            "On many places, the Food Stock was touching the walls.",
            "No pallets were being used and the food stock was touching the walls.",
            "Mosquitoes & Flies were found in Grocery & Back Room areas.",
            "Two Flycatchers were installed in the section but only one was in the working condition.",
            "4 packs of Toffu Paneer having the date of expiry on 27th November were placed with fresh paneer in the dairy chiller with no flag for identification of near expiry date.",
            "2 packs of Yakut LCS Sherohos with Mfg. date 13th October and 40 Days of Best Before were found at the retail counter at the time of the audit.",
            "3 packs of French Fries (McCain) were found in torn-off condition in the freezer.",
            "The date of Pkg. was missing on the Egg Trays.",
            "The complete Lot of Cake (Packaged) had date of packaging - November 2015 with Expiry date of January 2015.",
            "No records of pest control activity in the store were found for the month of November 2015.",
            "Dead Lizard in the decomposed state was found in the Rodent box."};

   public static final String[] comments = {
            "FSSAI licence was not available in the store.",
            "Food handler's nails were not found trimmed, also nail enamel was put on.",
            "The sliding window channel of the freezer was dirty (dust & deposition found).", "The cutting chopper (Mutton) was found dirty; Blood stains and meat particles were seen.",
            "Dirty uniforms were found placed in the meat section.",
            "Food handler was wearing the rings while on operations.",
            "Two dirty aprons were found placed in the meat shop.",
            "One dirty apron was found placed in the meat section.",
            "The food handler in the butchery was not fully aware of the guidelines on hand wash activity. ",
            "The food handler was not aware about the hand wash activity.",
            "Water Leakage observed from the hand wash sink tap.",
            "One dirty apron & two dirty white coats were found in the meat section.",
            "Two dirty aprons and one dirty white coat were found in the meat section.",
            "Food handler was found wearing a wrist band.",
            "The food handler was not aware about the hand wash activity. ",
            "Waste filters were found missing in the hand wash sink.",
            "The food handler's nails were not found trimmed.",
            "Two packs of chewing tobacco were found in the food handler's pocket.",
            "Hand wash sink was found dirty.",
            "Food handler was not wearing the dress while on operations. ",
            "The food handler was not wearing the apron while on operations. ",
            "One dirty apron was found in the meat shop.",
            "One dirty apron & two dirty white coats were found placed in the meat section.",
            "Food handler was found wearing a dirty apron. Also, one dirty apron was found placed in the meat section.",
            "The food handler was not wearing gloves while on operations in the meat shop.",
            "Elbow lever was not found fixed on the elbow action tap.",
            "No apron was available to be used by the food handler in the meat section ",
            "Food Handler was found wearing a ring and a thread during meat operations.",
            "Food handler was found wearing a dirty apron.",
            "The food handler was not fully aware of using the elbow lever while washing hands.",
            "Food handler was wearing a dirty apron.",
            "One dirty apron was found in the meat section.",
            "Hand wash sink was found very dirty and dirty knives were found in sink.",
            "Food handler was wearing a red thread on the wrist.",
            "Hand wash sink was found dirty.",
            "The product temperature (Milk) in one of the dairy chillers was recorded as 10.4 degrees C.",
            "Product temperature (Aloo Tikki) in veg freezer was recorded as -5.6 degrees C.",
            "Product temperature (Milk) in one of the dairy chillers was recorded as 9.3 degrees C.",
            "The product temperatures (Green Peas) & (Masala Fries) in two of the freezers were recorded as -11.8 degrees C and -7.5 degrees C respectively.",
            "The product temperature (Yumeeiz Cheese Finger) in veg freezer was recorded as -8.5 degrees C.",
            "The product temperature (McCain Aloo Tikki) in veg freezer was recorded as -12.5 degrees C.",
            "The temperature log book was not found updated for the day of the audit i.e. 25.07.2016.",
            "The thermometer validation log book was not found updated since 13.06.2016.",
            "Ice formation found in the non-veg freezer.",
            "Thermometers were not found in the working condition.",
            "The temperature log book was not found updated for 7 am schedule on the day of the audit.",
            "The veg freezer was found overloaded and the product temperature (Pagro Frozen Mixed Vegetables) in the same freezer was recorded as -3.7 degrees C.",
            "No thermometer was available in the store. ",
            "The temperature log book was not found updated since June 26, 2016.",
            "The thermometer validation log book was not available in the store. ",
            "The thermometer was not found in the working condition.",
            "The validation log book for thermometer was not available in the store..",
            "Thermometer was not found in the working condition.",
            "The temperature log book was found verified by the same person from last few days.",
            "The temperature record log book was not found verified by respective TL / SM from 24th August, 2016.",
            "Incorrect Storage Practices: Sufficient space was not given on the back from inside of the dairy chiller to let the cool air go down the lower shelves.",
            "No thermometer was available in the store.",
            "The temperature log book was not found updated since morning.",
            "The thermometer validation records are not being maintained; store does not have a provision for the same.",
            "Ice formation was observed in the non-veg freezer.",
            "The non-veg freezer was found overloaded and the temperature was recorded as -15.9 degrees C.",
            "Ice formation was observed in the veg freezer.",
            "Ice formation was observed in the veg freezer. The problem of condensation water leakage was observed in the dairy chiller.",
            "The log book for thermometer calibration was not available in the store.",
            "No validation records for thermometer were available in the store.",
            "One of the freezers was found overloaded.",
            "Ice formation was observed in both the veg as well as in the non-veg freezer.",
            "Ice formation was observed in the veg freezer.",
            "The inner surface area of the raw meat chiller was found dirty.",
            "Some floor tiles were found broken.",
            "The ceiling fan was found dirty.",
            "The ceiling fan was found dirty. Also, Peeling of paint observed on the ceiling.",
            "The bottom surface of one of the dairy chillers was found dirty; deposition of milk etc. observed.",
            "Ice formation was observed in non-veg freezer.",
            "The test strip for measuring the concentrated solution was not available in the store.",
            "The chopping board was not found cleaned; Blood stains and meat particles were observed. Also, the knives were dirty.",
            "One approved chemical (Kleen Hand) was not available in the the store. ",
            "Fan covers and gaskets of the non-veg freezer were found dirty. The inside walls of one of the dairy chillers were found dirty; deposition found.",
            "The sanitized Food pans were not found shrink wrapped. Also, the food pans were not found placed on their appropriate location.",
            "Floor was found dirty; deposition observed.",
            "Fan cover of one of the dairy chillers was found dirty.",
            "The chopping board was found dirty",
            "The bottom surface of the veg freezer was found dirty; corns were found spread in the freezer.",
            "The fan covers of both the dairy chillers were found dirty.",
            "Fan covers of one dairy chiller were found dirty. The gaskets of one of the freezers were found dirty. ",
            "Fan covers of both the dairy chillers were found dirty.",
            "The sliding window channel of the veg freezer was found dirty; dirt and deposition observed.",
            "The chopping board was not found sanitized. ",
            "The testing strip (for sanitizer) was not available in the store. ",
            "All utensils were not found properly cleaned & sanitized, foul smell was also observed. ",
            "The testing strip (for sanitizer) was not available in the meat section.  ",
            "The bottom surface of the vendor's chiller was found dirty. ",
            "The gaskets, fan cover and bottom surface of one of the dairy chillers were found dirty. ",
            "The test strip for measuring the concentrated solution was not found in the store. ",
            "The chopping board was not found cleaned. ",
            "Seepage was observed on one of the walls; food stock was placed near-by the same wall. ",
            "The sliding window channel of the veg freezer was dirty. ",
            "Seepage was observed on the wall, also the paint was found peeled off. ",
            "The Gaskets and fan cover of one of the dairy chillers were dirty. The sliding window channel of the veg freezer was found dirty; deposition observed. ",
            "The food stock (Pulses etc) was directly placed on the floor, no pallets were being used. ",
            "The bottom surface of the dairy chiller was found rusted. ",
            "Veg and non-veg food articles were kept together in the same freezer. ",
            "The sliding window channel of the raw meat chiller was found rusted. ",
            "The magnetic belt (for holding knives) was found rusted. ",
            "Veg and non-veg food items were found placed together in the freezer.",
            "No pallets were being used and the food stock was directly placed on the floor. ",
            "A condensation water leakage problem was observed in one of the dairy chillers.",
            "The knife holder (Magnetic Belt) was found rusted, even the marks of rust could be seen on the knives. ",
            "The condensation water leakage was observed from the dairy chiller. ",
            "The food stock was directly placed on the floor; no pallets were being used. ",
            "A condensation water leakage problem was observed from one of the dairy chillers. ",
            "The water leakage from the geyser water pipe was observed. ",
            "The raw meat food pans were not found covered with shrink wrap. ",
            "A condensation water leakage problem was observed from the Air Conditioner. ",
            "The magnetic belt (Knife Holder) was found rusted. ",
            "The iron scooper was found rusted. ",
            "Hairs found in raw meat; proper cleaning not done. ",
            "The food stock was directly placed on the floor. ",
            "The bottom surface of one dairy chiller was found rusted. ",
            "The magnetic belt (for holding knives) was found rusted. The sliding window channel of the raw meat chiller was also found rusted. ",
            "Veg and non-veg packaged food articles were found placed together in the veg freezer. ",
            "Veg and non-veg food items were found placed together in the veg freezer. ",
            "The iron scooper (in the loose sugar container) was found rusted. ",
            "The Sliding window channel of raw meat chiller was found rusted. ",
            "The veg and non-veg food articles were stored together in veg freezer. ",
            "Veg and non-veg food products were found placed together inside the ice-cream freezer. ",
            "A condensation water leakage problem was observed from the air conditioner. ",
            "Food (Soft Drinks) and non-food (Cleaning Chemicals) were found placed together. ",
            "A condensation water leakage problem was observed from the dairy chiller.  ",
            "Food stock was directly placed on the floor; no pallets were being used. ",
            "The veg and non-veg food items were kept in the same freezer. ",
            "The condensation water leakage problem was observed from raw meat chiller and from the air conditioner. ",
            "Expired food: 1 pack of easy choice pulse (Moong Chilka) was found having PKD Dt. 04/12/2015 and BEST BEFORE mentioned as 03/05/2016. ",
            "Expired food: Two trays of Farm Fresh Eggs (60 eggs) having PKD Dt. 04.July.2016 and BEST BEFORE 21 days. 5 packs of Litchi Delight Juice (1Ltr each) having Mfg. Dt. 27.12.15 and 4 packs ",
            "(Same Product) having Mfg. Dt. 19.01.16 and BEST BEFORE mentioned as 6 months. 17 kg Tilda Basmati Rice having PKD Dt. 03/04/2014 and BEST BEFORE mentioned as 24 months. 2 jars of Dawat Brown Rice having PKD Dt. 19/06/2015 and BEST BEFORE mentioned as 12 months. 2 jars of Kissan Jam having PKD Dt. 18.03.2015 and BEST BEFORE mentioned as 12 months. ",
            "Expired food: 1 Jar of Dawat Brown Basmati Rice having PKD Dt. 18/06/2015 and BEST BEFORE - 12 months. ",
            "Expired food: 2 packs of Nylon's Garam Masala having Mfg. Dt. NOV 2015 and BEST BEFORE 8 months. ",
            "Expired food: 1 pack of Britannia Cake having PKD Dt. 22/04/16 and BEST BEFORE 3 months. ",
            "Expired food: 1 pack of 2 kg Masoor Whole having PKD Dt. 28/01/2016 and BEST BEFORE Dt. 27/06/2016. ",
            "Expired food: 6 packs of Large White Eggs having PKD Dt. 06/07/2016 and BEST BEFORE 21 days. ",
            "Expired food: 10 packs of Tilda Basmati Rice having PKD Dt: 03.04.2014 and BEST BEFORE:\n" + "24 months. 8 packs Tetley Lemon Tea having Mfg. Dt: 07.2015 and BEST BEFORE: 12 months. 1 bottle of Tasty Treat Mango Drink having Mfg. Dt: 15.12.2015 and BEST BEFORE: 6 months. ",
            "Expired food: 1 pack of Venky's Chicken Pops having Mfg. Dt. 15.01.2016, 1 pack of Chicken Meat Balls having Mfg Dt. 17.01.2016, 1 pack of Yummiez Punjabi Tikka having Pkg Dt. 09.01.2016 and the BEST BEFORE Dt. for all these food products was mentioned as 6months. ",
            "Expired food: 3 packs of Nilon's Garam Masala having PKD Dt. 11,2015 and BEST BEFORE - 8 months. 11 packs of Tasty Treat Navratan Mix having PKD. Dt. 20.JAN.2016 and BEST BEFORE - 6 months. 1 bottle Mountain Dew having Mfg. Dt. 28.04.2016 and BEST BEFORE - 3 months. 16 bottles of Appy Fizz having Mfg. Dt. 21.03.2016 and BEST BEFORE - 4 months. 6 packs of Britannia Cheese Slice having PKD Dt. NOV 2015 and BEST BEFORE - 9 months. ",
            "Expired food: 1 pack of Hen Large White Eggs (10 eggs) having PKD Dt. 11.07.2016 and BEST BEFORE mentioned as 21 days. Image not available as store staff removed the expired sample from the location. ",
            "Expired food: 1 pack of Chicken Meat Balls (Brand - Venky's) having Mfg. Dt. 17.01.2016 and BEST BEFORE mentioned as 6 months. ",
            "Expired food placed as Dump but no label declaration for identification: 1 pack of Golden Harvest Maida having PKD Dt. 20/04/2016 and BEST BEFORE 3 months. 1 pack of EM Cashew having PKD 28/12/2015 and BEST BEFORE Dt. 27/06/2016. 1 pack of  Bourbon Bliss Biscuits having PKD Dt. 06/01/16 and BEST BEFORE 6 months. 1 pack of EM Cardamon Green (Elaichi) PKD Dt. 22/01/2016 and BEST BEFORE Dt. 20/07/2016. 1 pack of Hi Fi Cashew Butter Cookies having PKD Dt. 31/10/15 and BEST BEFORE 6months. 1 pack of Mango Pickle PKD Dt. June 15 and BEST BEFORE 12 months. ",
            "Leakage observed from 2 packs of Sundrop Heart Oil. ",
            "2 bags of Whole Wheat Flour were found in the damaged condition. ",
            "4 Packs of EM Pistachio having PKD Dt. 28.07.2016 and BEST BEFORE Dt 27.07.2016. 3 packs of Tropicana Guava Delight Juice having PKD Dt. 25.01.2016 and 1 pack of same product having PKD Dt. 25.12.15 and BEST BEFORE 6 months. 1 pack of EC Cashew having PKD Dt. 28.01.2016 and BEST BEFORE Dt. 27.07.2016. 1 pack of Golden Harvest having PKD Dt. 28/03/2016 and BEST BEFORE 4 months (The Stock partially found in the grocery & in the clearance bin). ",
            "A 3 kg White pumpkin in the spoiled state was found in the FnV section. Store staff immediately took away so no image available. ",
            "Food pans in the display chiller were not found labeled with the date of receiving and/or Use- by-date. ",
            "Date of manufacture could not be recognized; 4 packs of MTS Rava Idli were found having a punching hole at the spot on the label where Mfg. date was printed. ",
            "2kg white pumpkin was found in the stale condition. ",
            "Insects were seen in the loose grain sack (15kg Arhar Dal). ",
            "Vegetables in the baskets were found dirty. ",
            "The loose grain rice stock was found infested in the container. ",
            "Fat wastage of mutton and spoiled chicken salami were found inside the display chiller. ",
            "Food pans in the display chiller were not found labeled neither with the date of receiving nor with Use-by-date. ",
            "FEFO Issue: The previously received 20 packs of Golden Harvest Rajma were found in the back room and the recently acquired were found on the retail shelves. ",
            "The food pan containing Whole Chicken was not found labeled with received on date and/or Use-by-date. ",
            "4 packs of Large Brown Eggs (10 eggs each) were found in damaged condition. ",
            "A loose grain stock (Sac of Chana) was found infested in the back room area. ",
            "The packages of Hariyali Tikka and Peri Peri Tikka were found in the open condition. ",
            "1 food pan in raw meat chiller was found without having sticker/label for received on/Use-By- Date. ",
            "The raw meat in storage was found without having the information about Received on and/or Use-by-date. ",
            "Some of the food packages (Pulses) were found air punctured; pin holes were clearly visible. ",
            "Infestation issue observed in one pack of Elina Rice (5kg). ",
            " Leakage (Almost emptied pack of Amul Milk) was found in the dairy chiller.",
            "Infestation observed in Loose Chana Dal (weight - 8kg). ",
            "4 packs of Fresh and Pure Honey were found in the spoiled state. ",
            "4 packs of Dawat Rice (5Kg each) were found infested. ",
            "Potatoes in many of the net bag packs were found in the rotten state.",
            "Papaya Fruit was found in the rotten state. ",
            "2 packs of Tropicana Orange Juice and 4 packs of Tropicana Pineapple Juice (1Ltr each) were found without having MFG/PKD and BEST BEFORE Date on the labels. ",
            "Incorrect labeling: The information about the received on and Use-By-Date was found missing on the labels. The labels on the food pans were found with only a date without having any\n" +
                    "explanation on the same. ",
            "One UV tube was not found in the working condition. ",
            "5 packs of Dawat Rice (5Kg each) were found under the infestation state in the back room area. ",
            "One of the UV tubes of the flycatcher was not found to be in the working condition.\n ",
            "Two UV tubes of the flycatcher were not found in the working condition. ",
            "One of the UV tubes of the flycatcher was not found in the the working condition. ",
            "No rodent box was available in the back area. ",
            "No rodent box was found placed in the back room area. ",
            "One ordinary tube was found plugged in instead of UV tube in the fly catcher. ",
            "No rodent box was available in the store. ",
            "One UV tube of the flycatcher was not found in the working condition. ",
            "One of the UV tubes in the fly catcher was not found in the working condition. ",
            "Four rodent boxes were found without the glue pad. ",
            "One rodent box was found without the glue pad. ",
            "One UV tube was not found in the working condition. Also, the tray was found full of dead flies. ",
            "Flycatcher tray was missing. Moreover, one of the UV tubes was not found in the working condition. ",
            "Rodent box was found without the glue pad. ",
            "One of the UV tubes was not found in the working condition. ",
            "The flycatcher tray was found missing. ",
            "One of the UV tubes in the flycatcher was not found in the working condition. ",
            "The flycatcher was not found in the working condition. ",
            "Rodent box was not found placed in the Fnv section. ",
            "One UV tube was not found in the working condition. ",
            "No rodent box was available. ",
            "Dead Lizard was found in the rodent box. ",
            "A large number of flies were seen in FnV section. ",
            "A dead rodent and insects were found in the rodent box. ",
            "Pest Evidence: A rodent was seen in the grocery section. ",
            "The flycatcher tray was found full of dead flies. ",
            "A dead rat was found in the rodent box. ",
            "The flycatcher tray was found full of dead flies. ",
            "Plenty of dead flies were found in the flycatcher tray. ",
            "A dead rat and insects were found in the rodent boxes. ",
            "A large number of flies were seen in the grocery section. ",
            "Dead flies were found in the flycatcher tray. ",
            "Two flycatcher trays were found full of dead flies. ",
            "The glue pad was found full of dead flies. ",
            "Dead rat was found in the rodent box. ",
            "A large numbers of flies were seen in the meat shop. ",
            "The 3 loose rice containers; Parimal Rice, Mini Mogra and Tibra Rice were found infested. ",
            "3 dead lizards were found in the rodent box. ",
            "8 packs of Basmati Rice (5kg each) were found infested. ",
            "Dead lizards and large number of flying insects were found in the rodent box. ",
            "Infestation Issue observed in 3 packs of Elina Rice and 2 packs of Devaaya Rice. ",
            "The flycatcher was found to be dirty; plenty of dead flies were seen on the frame. ",
            "Glue pad was found missing in the flycatcher. ",
            "The glue pad in flycatcher was found full of dead flies. ",
            "Pest program was not found updated since Sept. 06, 2016. ",
            "Dead flies were found in the flycatcher tray. ",
            "Flycatcher was found without having a glue pad and/or flycatcher tray. ",
            "Cockroaches were seen roving around in the back room area. ",
            "No exceptions found ",
            "Dead lizards and insects were found in two rodent boxes. ",
            "Food handler was wearing a rubber bracelet while on operations. ",
            "15Kg stock of Arhar Daal was found infested in the back room and was stored alongside regular food stock. No identification mark such as dump area/not for sale etc. could be observed by the auditor at the time of the inspection. Auditor was not allowed to take picture. ",
            "A large number of flies were seen in the FnV section.",
            "The knife holder (Magnetic Belt) was found rusted; the rusting marks were also observed on the knives. ",
            "No Exceptions Found. ",
            "Not Applicable. ",
            "FSSAI License was not displayed at the prominent place; it was displayed in the back room. ",
            "There was Flaking/Peeling of walls observed. ",
            "The sliding window panel of the display chiller was dirty. ",
            "Gaskets of the chiller were not properly cleaned. ",
            "The entrance door of the meat shop was dirty. ",
            "The Gaskets of the chiller and freezer were dirty. ",
            "No records for cleaning of chillers & freezers were being maintained. ",
            "Air cutter was not in operation. Fly catcher was installed at a height of above 6ft. ",
            "Flaking/Peeling of paint found on one of the walls close to the display chiller in the Meat shop.  ",
            "Gaskets of the chillers in the Meat shop were dirty. ",
            "Flycatcher was installed at above 7ft. height in the Meat shop. ",
            "Flycatcher was not working in the Back room and was installed at above 7ft. height. ",
            "The iron frames for keeping vegetable baskets were not properly cleaned. ",
            "Gaskets were not properly cleaned in two chillers in the Grocery. ",
            "Vegetable basket frames were dirty in Fruits & Vegetable section. ",
            "Gaskets and Doors were found dirty both in chiller and freezer in the Meat shop.  ",
            "No sanitizer available and only floor cleaning chemical was found in the store.  ",
            "Dirty basket and dirty Mop was placed in the meat shop. ",
            "Flycatcher was not in working condition both in the Meat Shop & Grocery section. ",
            "Gaskets of the Chillers & Freezers were not properly cleaned in the Grocery section. ",
            "The cooling fans of the chillers were dirty in the Grocery section. ",
            "Only one approved chemical was found in the store. ",
            "Only one UV tube was working in the flycatcher both in the Back room & FnV sections. ",
            "Flycatchers were installed at a above 6ft. height in the store. ",
            "Hand Wash Sinks were dirty in the meat shop. ",
            "The floor was found to be dirty in the meat Shop. ",
            "Floors were not properly cleaned in the Grocery, some food material and cart board were lying on the floor. ",
            "The freezer in the meat shop was found to be dirty both from inside and outside. ",
            "Flies were found in the Meat Section. ",
            "Air cutter of the Meat section was not in operation. ",
            "Iron Frames in the Fruits & Vegetable section were not properly cleaned. ",
            "One of the Chillers’ cooling fan was dirty in the Grocery Section. ",
            "The chillers' fan covers & Gaskets were dirty. ",
            "Fly catcher was installed at a height of above 7ft and only one UV tube was working. ",
            "Food Handlers were using Dettol Soap for washing hands in the store. ",
            "The temperature inside the freezer was recorded at -15 Degrees Celsius. ",
            "The Gaskets and the Fan Covers of the chillers were dirty.  ",
            "Hand marks/stains were observed on the glass doors of the chillers. ",
            "Four packets of Chicken Nuggets were found alongside the Veg Products (Frozen Peas) in the freezer. ",
            "Fly Catcher was installed at a height of above 7ft and only one UV tube was in working condition. ",
            "There was rust on the bottom body part of the chillers from inside. ",
            "One of the UV tubes in the flycatcher was not working. ",
            "The Gaskets and the fan covers of both the chillers were dirty. ",
            "No Approved Chemicals were found in the store. ",
            "Floor was dirty; lot of footmarks and the deposition of dirt were observed. ",
            "A dead flying insect was found in one of the chillers.  ",
            "The fan covers of the chillers were dirty. ",
            "Sufficient distance was not maintained between the Iron racks and the walls. ",
            "The merchandizing shelves were attached to the walls.   ",
            "The floor of the chiller zone was not properly cleaned.  ",
            "The fan covers of both chillers were dirty.  ",
            "The merchandizing shelves of the chillers were found to be dirty.  ",
            "The Gaskets of the freezers were not properly cleaned. ",
            "No Approved Chemicals were available in the store. ",
            "Frozen Veg food products like Aloo Tikki, Frozen Peas etc. were placed along with Non-Veg frozen food products in the freezer. ",
            "Packs of Basmati Rice were placed directly on the floor. ",
            "Edible oil was placed alongside the detergent. ",
            "Bad Storage Practices; Food & Non-Food Articles were placed together. ",
            "Both Dairy Chillers were overloaded with Food Stock. ",
            "Approx. 100 packs of frozen Veg Food Products were kept in the chillers. ",
            "Freezer was overloaded with Food Stock. ",
            "Mosquitoes & Flies were found in Grocery & Back Room areas. ",
            "Two Flycatchers were installed in the section but only one was in the working condition. ",
            "4 packs of Toffu Paneer having the date of expiry on 27th November were placed with fresh paneer in the dairy chiller with no flag for identification of near expiry date.  ",
            "2 packs of Yakut LCS Sherohos with Mfg. date 13th October and 40 Days of Best Before were found at the retail counter at the time of the audit. ",
            "3 packs of French Fries (McCain) were found in torn-off condition in the freezer. ",
            "The complete Lot of Cake (Packaged) had date of packaging - November 2015 with Expiry date of January 2015. ",
            "No records of pest control activity in the store were found for the month of November 2015. ",
            "Dead Lizard in the decomposed state was found in the Rodent box."
    };






}

# example-grpc-java
- https://grpc.io/docs/languages/java/
  - https://github.com/grpc/grpc-java/blob/master/examples/src/main/proto/helloworld.proto
  - https://github.com/grpc/grpc-java/tree/master/examples/src/main/java/io/grpc/examples/helloworld
- https://github.com/grpc/grpc-java


## hello

Client:

```shell
Jul 31, 2024 5:07:35 PM com.spike.grpc.hello.HelloWorldClient greet
INFO: Will try to greet world ...
Jul 31, 2024 5:07:35 PM com.spike.grpc.hello.HelloWorldClient greet
INFO: Greeting: Hello world
```

Server:

```shell
Jul 31, 2024 5:07:09 PM com.spike.grpc.hello.HelloWorldServer start
INFO: Server started, listening on 50051
Jul 31, 2024 5:07:26 PM com.spike.grpc.hello.HelloWorldServer$GreeterImpl sayHello
INFO: >>> world

# IDEA > Debug > Threads > main Interrupt
Exception in thread "main" java.lang.InterruptedException
	at java.base/java.lang.Object.wait(Native Method)
	at java.base/java.lang.Object.wait(Object.java:338)
	at io.grpc.internal.ServerImpl.awaitTermination(ServerImpl.java:322)
	at com.spike.grpc.hello.HelloWorldServer.blockUntilShutdown(HelloWorldServer.java:70)
	at com.spike.grpc.hello.HelloWorldServer.main(HelloWorldServer.java:80)
	at com.spike.grpc.App.main(App.java:13)
*** shutting down gRPC server since JVM is shutting down
*** server shut down
```

## route guide

Client:

```shell
Jul 31, 2024 6:50:15 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: *** GetFeature: lat=409,146,138 lon=-746,188,906
Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Found feature called "Berkshire Valley Management Area Trail, Jefferson, NJ, USA" at 40.915, -74.619
Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: *** GetFeature: lat=0 lon=0
Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Found no feature at 0, 0
Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: *** ListFeatures: lowLat=400,000,000 lowLon=-750,000,000 hiLat=420,000,000 hiLon=-730,000,000
Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #1: name: "Patriots Path, Mendham, NJ 07945, USA"
location {
  latitude: 407838351
  longitude: -746143763
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #2: name: "101 New Jersey 10, Whippany, NJ 07981, USA"
location {
  latitude: 408122808
  longitude: -743999179
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #3: name: "U.S. 6, Shohola, PA 18458, USA"
location {
  latitude: 413628156
  longitude: -749015468
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #4: name: "5 Conners Road, Kingston, NY 12401, USA"
location {
  latitude: 419999544
  longitude: -740371136
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #5: name: "Mid Hudson Psychiatric Center, New Hampton, NY 10958, USA"
location {
  latitude: 414008389
  longitude: -743951297
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #6: name: "287 Flugertown Road, Livingston Manor, NY 12758, USA"
location {
  latitude: 419611318
  longitude: -746524769
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #7: name: "4001 Tremley Point Road, Linden, NJ 07036, USA"
location {
  latitude: 406109563
  longitude: -742186778
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #8: name: "352 South Mountain Road, Wallkill, NY 12589, USA"
location {
  latitude: 416802456
  longitude: -742370183
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #9: name: "Bailey Turn Road, Harriman, NY 10926, USA"
location {
  latitude: 412950425
  longitude: -741077389
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #10: name: "193-199 Wawayanda Road, Hewitt, NJ 07421, USA"
location {
  latitude: 412144655
  longitude: -743949739
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #11: name: "406-496 Ward Avenue, Pine Bush, NY 12566, USA"
location {
  latitude: 415736605
  longitude: -742847522
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #12: name: "162 Merrill Road, Highland Mills, NY 10930, USA"
location {
  latitude: 413843930
  longitude: -740501726
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #13: name: "Clinton Road, West Milford, NJ 07480, USA"
location {
  latitude: 410873075
  longitude: -744459023
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #14: name: "16 Old Brook Lane, Warwick, NY 10990, USA"
location {
  latitude: 412346009
  longitude: -744026814
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #15: name: "3 Drake Lane, Pennington, NJ 08534, USA"
location {
  latitude: 402948455
  longitude: -747903913
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #16: name: "6324 8th Avenue, Brooklyn, NY 11220, USA"
location {
  latitude: 406337092
  longitude: -740122226
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #17: name: "1 Merck Access Road, Whitehouse Station, NJ 08889, USA"
location {
  latitude: 406421967
  longitude: -747727624
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #18: name: "78-98 Schalck Road, Narrowsburg, NY 12764, USA"
location {
  latitude: 416318082
  longitude: -749677716
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #19: name: "282 Lakeview Drive Road, Highland Lake, NY 12743, USA"
location {
  latitude: 415301720
  longitude: -748416257
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #20: name: "330 Evelyn Avenue, Hamilton Township, NJ 08619, USA"
location {
  latitude: 402647019
  longitude: -747071791
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #21: name: "New York State Reference Route 987E, Southfields, NY 10975, USA"
location {
  latitude: 412567807
  longitude: -741058078
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #22: name: "103-271 Tempaloni Road, Ellenville, NY 12428, USA"
location {
  latitude: 416855156
  longitude: -744420597
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #23: name: "1300 Airport Road, North Brunswick Township, NJ 08902, USA"
location {
  latitude: 404663628
  longitude: -744820157
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #24: name: "211-225 Plains Road, Augusta, NJ 07822, USA"
location {
  latitude: 411633782
  longitude: -746784970
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #25: name: "165 Pedersen Ridge Road, Milford, PA 18337, USA"
location {
  latitude: 413447164
  longitude: -748712898
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #26: name: "100-122 Locktown Road, Frenchtown, NJ 08825, USA"
location {
  latitude: 405047245
  longitude: -749800722
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #27: name: "650-652 Willi Hill Road, Swan Lake, NY 12783, USA"
location {
  latitude: 417951888
  longitude: -748484944
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #28: name: "26 East 3rd Street, New Providence, NJ 07974, USA"
location {
  latitude: 407033786
  longitude: -743977337
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #29: name: "611 Lawrence Avenue, Westfield, NJ 07090, USA"
location {
  latitude: 406589790
  longitude: -743560121
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #30: name: "18 Lannis Avenue, New Windsor, NY 12553, USA"
location {
  latitude: 414653148
  longitude: -740477477
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #31: name: "82-104 Amherst Avenue, Colonia, NJ 07067, USA"
location {
  latitude: 405957808
  longitude: -743255336
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #32: name: "170 Seven Lakes Drive, Sloatsburg, NY 10974, USA"
location {
  latitude: 411733589
  longitude: -741648093
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #33: name: "1270 Lakes Road, Monroe, NY 10950, USA"
location {
  latitude: 412676291
  longitude: -742606606
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #34: name: "509-535 Alphano Road, Great Meadows, NJ 07838, USA"
location {
  latitude: 409224445
  longitude: -748286738
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #35: name: "652 Garden Street, Elizabeth, NJ 07202, USA"
location {
  latitude: 406523420
  longitude: -742135517
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #36: name: "349 Sea Spray Court, Neptune City, NJ 07753, USA"
location {
  latitude: 401827388
  longitude: -740294537
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #37: name: "13-17 Stanley Street, West Milford, NJ 07480, USA"
location {
  latitude: 410564152
  longitude: -743685054
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #38: name: "47 Industrial Avenue, Teterboro, NJ 07608, USA"
location {
  latitude: 408472324
  longitude: -740726046
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #39: name: "5 White Oak Lane, Stony Point, NY 10980, USA"
location {
  latitude: 412452168
  longitude: -740214052
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #40: name: "Berkshire Valley Management Area Trail, Jefferson, NJ, USA"
location {
  latitude: 409146138
  longitude: -746188906
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #41: name: "1007 Jersey Avenue, New Brunswick, NJ 08901, USA"
location {
  latitude: 404701380
  longitude: -744781745
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #42: name: "6 East Emerald Isle Drive, Lake Hopatcong, NJ 07849, USA"
location {
  latitude: 409642566
  longitude: -746017679
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #43: name: "1358-1474 New Jersey 57, Port Murray, NJ 07865, USA"
location {
  latitude: 408031728
  longitude: -748645385
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #44: name: "367 Prospect Road, Chester, NY 10918, USA"
location {
  latitude: 413700272
  longitude: -742135189
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #45: name: "10 Simon Lake Drive, Atlantic Highlands, NJ 07716, USA"
location {
  latitude: 404310607
  longitude: -740282632
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #46: name: "11 Ward Street, Mount Arlington, NJ 07856, USA"
location {
  latitude: 409319800
  longitude: -746201391
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #47: name: "300-398 Jefferson Avenue, Elizabeth, NJ 07201, USA"
location {
  latitude: 406685311
  longitude: -742108603
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #48: name: "43 Dreher Road, Roscoe, NY 12776, USA"
location {
  latitude: 419018117
  longitude: -749142781
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #49: name: "Swan Street, Pine Island, NY 10969, USA"
location {
  latitude: 412856162
  longitude: -745148837
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #50: name: "66 Pleasantview Avenue, Monticello, NY 12701, USA"
location {
  latitude: 416560744
  longitude: -746721964
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #51: name: "565 Winding Hills Road, Montgomery, NY 12549, USA"
location {
  latitude: 415534177
  longitude: -742900616
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #52: name: "231 Rocky Run Road, Glen Gardner, NJ 08826, USA"
location {
  latitude: 406898530
  longitude: -749127080
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #53: name: "100 Mount Pleasant Avenue, Newark, NJ 07104, USA"
location {
  latitude: 407586880
  longitude: -741670168
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #54: name: "517-521 Huntington Drive, Manchester Township, NJ 08759, USA"
location {
  latitude: 400106455
  longitude: -742870190
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #55: name: "40 Mountain Road, Napanoch, NY 12458, USA"
location {
  latitude: 418803880
  longitude: -744102673
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #56: name: "48 North Road, Forestburgh, NY 12777, USA"
location {
  latitude: 415464475
  longitude: -747175374
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #57: name: "9 Thompson Avenue, Leonardo, NJ 07737, USA"
location {
  latitude: 404226644
  longitude: -740517141
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #58: name: "213 Bush Road, Stone Ridge, NY 12484, USA"
location {
  latitude: 418811433
  longitude: -741718005
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #59: name: "1-17 Bergen Court, New Brunswick, NJ 08901, USA"
location {
  latitude: 404839914
  longitude: -744759616
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #60: name: "35 Oakland Valley Road, Cuddebackville, NY 12729, USA"
location {
  latitude: 414638017
  longitude: -745957854
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #61: name: "42-102 Main Street, Belford, NJ 07718, USA"
location {
  latitude: 404318328
  longitude: -740835638
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #62: name: "3387 Richmond Terrace, Staten Island, NY 10303, USA"
location {
  latitude: 406411633
  longitude: -741722051
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #63: name: "261 Van Sickle Road, Goshen, NY 10924, USA"
location {
  latitude: 413069058
  longitude: -744597778
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Result #64: name: "3 Hasta Way, Newton, NJ 07860, USA"
location {
  latitude: 410248224
  longitude: -747127767
}

Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: *** RecordRoute
Jul 31, 2024 6:50:16 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 40.641, -74.172
Jul 31, 2024 6:50:17 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 40.126, -74.796
Jul 31, 2024 6:50:18 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 41.902, -74.914
Jul 31, 2024 6:50:19 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 41.384, -74.05
Jul 31, 2024 6:50:20 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 41.087, -74.446
Jul 31, 2024 6:50:21 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 40.711, -74.975
Jul 31, 2024 6:50:22 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 41.795, -74.848
Jul 31, 2024 6:50:22 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 41.961, -74.652
Jul 31, 2024 6:50:24 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 41.422, -74.333
Jul 31, 2024 6:50:24 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Visiting point 40.531, -74.984
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Finished trip with 10 points. Passed 6 features. Travelled 799,584 meters. It took 9 seconds.
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Finished RecordRoute
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: *** RouteChat
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Sending message "First message" at 0, 0
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Sending message "Second message" at 0, 10,000,000
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Sending message "Third message" at 10,000,000, 0
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Sending message "Fourth message" at 10,000,000, 10,000,000
Jul 31, 2024 6:50:25 PM com.spike.grpc.routeguide.RouteGuideClient info
INFO: Finished RouteChat
```

Server:

```shell
Jul 31, 2024 6:50:12 PM com.spike.grpc.routeguide.RouteGuideServer start
INFO: Server started, listening on 8980

Exception in thread "main" java.lang.InterruptedException
	at java.base/java.lang.Object.wait(Native Method)
	at java.base/java.lang.Object.wait(Object.java:338)
	at io.grpc.internal.ServerImpl.awaitTermination(ServerImpl.java:322)
	at com.spike.grpc.routeguide.RouteGuideServer.blockUntilShutdown(RouteGuideServer.java:106)
	at com.spike.grpc.routeguide.RouteGuideServer.main(RouteGuideServer.java:119)
*** shutting down gRPC server since JVM is shutting down
*** server shut down
```
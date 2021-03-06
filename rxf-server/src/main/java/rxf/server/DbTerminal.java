package rxf.server;

import org.intellij.lang.annotations.Language;
import rxf.server.an.DbKeys.etype;
import rxf.server.driver.CouchMetaDriver;

import java.nio.ByteBuffer;
import java.util.EnumSet;

public enum DbTerminal {
    /**
     * results are squashed.
     */
    oneWay {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {
            return (implementation ? "public " : "") + "void " + name() + "()" + (implementation ? "{\n    final DbKeysBuilder dbKeysBuilder=(DbKeysBuilder)DbKeysBuilder.get();\n" +
                    "final ActionBuilder actionBuilder=(ActionBuilder )ActionBuilder.get();" +
                    "\ndbKeysBuilder.validate();\n BlobAntiPatternObject.EXECUTOR_SERVICE.submit(new Runnable(){" +
                    "\npublic void run(){\n" + "    try{\n\n      DbKeysBuilder.currentKeys.set(dbKeysBuilder);   \n      ActionBuilder.currentAction.set(actionBuilder); \nrxf.server.driver.CouchMetaDriver." + couchDriver + ".visit();\n}catch(Exception e){\n    e.printStackTrace();}\n    }\n    });\n}" : ";");
        }
    },
    /**
     * returns resultset from the assumed Future<couchTx>
     */
    rows {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {


            String visitor = "rxf.server.driver.CouchMetaDriver." + couchDriver;
            String cmdName = couchDriver.name();
            @Language("JAVA") String s = "{\n" +
                    "            try {\n" +
                    "              return    BlobAntiPatternObject.GSON.fromJson(one.xio.HttpMethod.UTF8.decode(avoidStarvation(" +
                    visitor + ".visit())).toString(),\n" +
                    "                  new java.lang.reflect.ParameterizedType() {\n" +
                    "                    public Type getRawType() {\n" +
                    "                      return CouchResultSet.class;\n" +
                    "                    }\n" +
                    "\n" +
                    "                    public Type getOwnerType() {\n" +
                    "                      return null;\n" +
                    "                    }\n" +
                    "\n" +
                    "                    public Type[] getActualTypeArguments() {\n" +
                    "                             " +
                    "                          Type[]t={(Type)" + cmdName + ".this.get(DbKeys.etype.type)};" +
                    "                          return t;\n" +
                    "                    }\n" +
                    "                  });\n" +
                    "            } catch (Exception e) {\n" +
                    "              e.printStackTrace();\n" +
                    "            }\n" +
                    "            return null;\n" +
                    "          }";
            return (implementation ? "public " : "") + CouchResultSet.class.getCanonicalName() + " " + name() + "()" + (implementation ? s : ";");
        }
    },
    /**
     * returns resultset from the assumed Future<couchTx>
     */
    pojo {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {
            EnumSet<etype> of = EnumSet.of(parms[0], parms);
            assert of.contains(etype.type);

            return (implementation ? " public " : "") +
                    ByteBuffer.class.getCanonicalName() +
                    " " + name() + "()" + (implementation ?
                    "{ \n" +
                            "try {\n" +
                            "        return (" +
                            ByteBuffer.class.getCanonicalName() +
                            ") rxf.server.driver.CouchMetaDriver." + couchDriver +
                            ".visit();\n" +
                            "      } catch (Exception e) {\n" +
                            "        e.printStackTrace();   \n" +
                            "      } " +
                            "        return null;} " : ";");
        }
    },
    /**
     * returns couchTx from the assumed Future<couchTx>
     */
    tx {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {
            return (implementation ? " public " : "") + " CouchTx tx()" + (implementation ?
                    "{try {\n" +
                            "        return (CouchTx)rxf.server.BlobAntiPatternObject.GSON.fromJson(one.xio.HttpMethod.UTF8.decode(" +
                            " rxf.server.driver.CouchMetaDriver." + couchDriver + ".visit()).toString(),CouchTx.class);\n" +
                            "      } catch (Exception e) {\n" +
                            "        if(rxf.server.BlobAntiPatternObject.DEBUG_SENDJSON)e.printStackTrace();   \n" +
                            "      } return null;} " : ";");
        }
    },
    /**
     * returns the Future<?> used.
     */
    future {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {
            return
                    (implementation ? "public " : "") + "Future<ByteBuffer>future()" +
                            (implementation ? "{\n    try{\n    BlobAntiPatternObject.EXECUTOR_SERVICE.submit(new Callable<ByteBuffer>(){\nfinal DbKeysBuilder dbKeysBuilder=(DbKeysBuilder)DbKeysBuilder.get();\n" +
                                    "final ActionBuilder actionBuilder=(ActionBuilder)ActionBuilder.get();\n"
                                    +
                                    "public " + ByteBuffer.class.getCanonicalName() + " call() throws Exception{" +
                                    "    DbKeysBuilder.currentKeys.set(dbKeysBuilder);" +
                                    "\nActionBuilder.currentAction.set(actionBuilder);\n" +
                                    "return(" + ByteBuffer.class.getCanonicalName() + ")rxf.server.driver.CouchMetaDriver." + couchDriver + ".visit(dbKeysBuilder,actionBuilder);\n}" +
                                    "\n    }" +
                                    "\n    );" +
                                    "\n} catch(Exception e){\n    e.printStackTrace();\n}\n    return null;\n}" : ";");
        }
    },
    /**
     * follows the _changes semantics with potential 1-byte chunksizes.
     */
    continuousFeed {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {
            return (implementation ? " public " : "") + " void " + name() + "()" + (implementation ? ("{" + BIG_EMPTY_PLACE + "} ") : ";\n");
        }
    }, json {
        public String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation) {
            @Language("JAVA") String s = "{\n    try{\n" +
                    "    ByteBuffer visit=rxf.server.driver.CouchMetaDriver." + couchDriver.name() +
                    ".visit();\n" +
                    "return null==visit?null:one.xio.HttpMethod.UTF8.decode(avoidStarvation(visit)).toString();\n" +
                    "}catch(Exception e){\n" +
                    "    e.printStackTrace();\n" +
                    "}\n" +
                    "    return null;\n}";
            return (implementation ? " public " : "") + " String  json()" + (implementation ?
                    s : ";");
        }
    };
    public static final String BIG_EMPTY_PLACE = "throw new AbstractMethodError();";

    public abstract String builder(CouchMetaDriver couchDriver, etype[] parms, boolean implementation);
};

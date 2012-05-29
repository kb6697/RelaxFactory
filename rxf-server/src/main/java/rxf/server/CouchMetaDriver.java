package rxf.server;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.SynchronousQueue;

import org.intellij.lang.annotations.Language;
import rxf.server.DbKeys.ReturnAction;
import rxf.server.DbKeys.etype;

import static rxf.server.DbKeys.etype.blob;
import static rxf.server.DbKeys.etype.db;
import static rxf.server.DbKeys.etype.designDocId;
import static rxf.server.DbKeys.etype.docId;
import static rxf.server.DbKeys.etype.mimetype;
import static rxf.server.DbKeys.etype.opaque;
import static rxf.server.DbKeys.etype.rev;
import static rxf.server.DbKeys.etype.validjson;
import static rxf.server.DbKeys.etype.view;
import static rxf.server.DbTerminal.continuousFeed;
import static rxf.server.DbTerminal.future;
import static rxf.server.DbTerminal.oneWay;
import static rxf.server.DbTerminal.pojo;
import static rxf.server.DbTerminal.rows;
import static rxf.server.DbTerminal.tx;


/**
 * confers traits on an oo platform...
 * <p/>
 * User: jim
 * Date: 5/24/12
 * Time: 3:09 PM
 */
public enum CouchMetaDriver {

  @DbTask({tx, oneWay}) @DbKeys({db, docId})createDb,
  @DbTask({tx, oneWay}) @DbKeys({db, docId, validjson})createDoc,
  @DbTask({pojo, future}) @DbResultUnit(String.class) @DbKeys({db, docId})getDoc,
  @DbTask({tx, future}) @DbResultUnit(String.class) @DbKeys({db, docId})getRevision,
  @DbTask({tx, oneWay, future}) @DbKeys({db, docId, rev, validjson})updateDoc,
  @DbTask({tx, oneWay}) @DbKeys({db, designDocId, validjson})createNewDesignDoc,
  @DbTask({tx}) @DbResultUnit(String.class) @DbKeys({db, designDocId})getDesignDoc,
  @DbTask({tx, oneWay}) @DbKeys({db, designDocId, validjson})updateDesignDoc,
  @DbTask({rows, future, continuousFeed}) @DbResultUnit(CouchResultSet.class) @DbKeys({db, view})getView,
  @DbTask({tx, oneWay, rows, future, continuousFeed}) @DbKeys({opaque, validjson})sendJson {
    @Override
    <T> Object visit(final DbKeysBuilder<T> dbKeysBuilder, final ActionBuilder<T> actionBuilder, ReturnAction<T> terminalBuilder) throws Exception {
      String opaque = (String) dbKeysBuilder.getParms().get(etype.opaque);
      String validjson = (String) dbKeysBuilder.getParms().get(etype.validjson);
      return BlobAntiPatternObject.sendJson(opaque, validjson);
    }
  },
  @DbTask({tx, future, oneWay}) @DbResultUnit(Rfc822HeaderState.class) @DbKeys({opaque, mimetype, blob})sendBlob {};

  public static final String XXXXXXXXXXXXXXMETHODS = "/*XXXXXXXXXXXXXXMETHODS*/";
  public static final String BAKED_IN_FIRE = "BAKED_IN_FIRE";

  <T> Object visit() throws Exception {
    DbKeysBuilder<T> dbKeysBuilder = (DbKeysBuilder<T>) DbKeysBuilder.currentKeys.get();
    ActionBuilder<T> actionBuilder = (ActionBuilder<T>) ActionBuilder.currentAction.get();
    ReturnAction<T> returnAction = (ReturnAction<T>) ReturnAction.currentResults.get();
    return visit(dbKeysBuilder, actionBuilder, returnAction);
  }

  /*abstract */<T> Object visit(DbKeysBuilder<T> dbKeysBuilder,
                                ActionBuilder<T> actionBuilder,
                                ReturnAction<T> terminalBuilder) throws Exception {
    throw new AbstractMethodError();
  }

  ;

  public static final String XDEADBEEF_2 = "-0xdeadbeef.2";


  public <T> String builder() throws NoSuchFieldException {
    Field field = CouchMetaDriver.class.getField(name());


    String s = null;
    if (field.getType().isAssignableFrom(CouchMetaDriver.class)) {
      CouchMetaDriver couchDriver = CouchMetaDriver.valueOf(field.getName());

      etype[] parms = field.getAnnotation(DbKeys.class).value();
      Class rtype = CouchTx.class;
      try {
        rtype = field.getAnnotation(DbResultUnit.class).value();
      } catch (Exception e) {
      }
      final String cn = rtype.getCanonicalName();
      @Language("JAVA")
      final String s2 = "\n\npublic class _ename_Builder <T>extends DbKeysBuilder<" + cn + "> {\n" +
          "    Rfc822HeaderState rfc822HeaderState;\n" +
          "    java.util.EnumMap<etype, Object> parms = new java.util.EnumMap<etype, Object>(etype.class);\n" +
          "  private SynchronousQueue<T>[] dest;\n\n" +
          "  @Override\n" +
          "    public ActionBuilder<" + cn + "> to(SynchronousQueue<" +
          cn + ">...dest) {\n    this.dest = dest;\n    if (parms.size() == parmsCount)\n            return new ActionBuilder<" + cn + ">() {\n                @Override\n" +
          "                public AbstractTerminalBuilder<" + cn + "> fire() {\n" +
          "                    return new AbstractTerminalBuilder <" + cn + ">(){" +
          BAKED_IN_FIRE + "};\n" +
          "                }\n" +
          "            };\n" +
          "        throw new IllegalArgumentException(\"required parameters are: " + BlobAntiPatternObject.arrToString(parms) + "\");\n" +
          "    }\n" +
          "    " + XXXXXXXXXXXXXXMETHODS + "\n" +
          "}\n";
      s = s2;
      int vl = parms.length;
      String s1 = "\nstatic private final int parmsCount=" + XDEADBEEF_2 + ";\n";
      for (int i = 0; i < vl; i++) {
        etype etype = parms[i];
        String name = etype.name();
        Class<? extends Object> clazz = etype.clazz;
        @Language("JAVA") String y = "public _ename_Builder _name_(_clazz_ _sclazz_){parms.put(DbKeys.etype." + etype.name() + ",_sclazz_);return this;}\n";
        s1 += y.replace("_name_", etype.name()).replace("_clazz_", clazz.getCanonicalName()).replace("_sclazz_", clazz.getSimpleName().toLowerCase()).replace("_ename_", name());
      }
      {

        final DbTask annotation = field.getAnnotation(DbTask.class);
        if (null != annotation) {
          final DbTerminal[] terminals = annotation.value();
          String t = "";
          for (DbTerminal terminal : terminals) {
            t += terminal.builder(couchDriver, parms, rtype
            );


          }
          s = s.replace(BAKED_IN_FIRE, t);
        }
      }

      s = s.replace(XXXXXXXXXXXXXXMETHODS, s1).replace("_ename_", name()).replace(XDEADBEEF_2, String.valueOf(vl));
    }
    return s;
  }

  public static void main(String... args) throws NoSuchFieldException {
    Field[] fields = CouchMetaDriver.class.getFields();
    @Language("JAVA")
    String s = "package rxf.server;\n//generated\nimport java.util.concurrent.*;\nimport java.util.*;\nimport static rxf.server.DbKeys.*;\nimport static rxf.server.DbKeys.etype.*;\nimport static rxf.server.CouchMetaDriver.*;\nimport java.util.*;\n\n/**\n * generated drivers\n */\npublic interface CouchDriver{";
    for (Field field : fields)
      if (field.getType().isAssignableFrom(CouchMetaDriver.class)) {
        CouchMetaDriver couchDriver = CouchMetaDriver.valueOf(field.getName());
        DbKeys dbKeys = field.getAnnotation(DbKeys.class);
        etype[] value = dbKeys.value();
        {

          DbResultUnit annotation = field.getAnnotation(DbResultUnit.class);
          s += null != annotation ? annotation.value().getCanonicalName() : CouchTx.class.getCanonicalName();
          s += ' ' + couchDriver.name() + '(';
          Iterator<etype> iterator = Arrays.asList(value).iterator();
          while (iterator.hasNext()) {
            etype etype = iterator.next();
            s += " " +
                etype.clazz.getCanonicalName() + " " + etype.name();
            if (iterator.hasNext())
              s += ',';
          }
          s += " );\n";
          String builder = couchDriver.builder();
          s += "\n" + builder;
        }
      }
    s += "}";
    System.out.println(s);
  }

  public static ThreadLocal<SynchronousQueue[]> currentSync = new ThreadLocal<SynchronousQueue[]>();

  /**
   * try to bind in common couchDriver stuff
   */
  public void sanityCheck() {
    new DbKeysBuilder() {
      @Override
      public ActionBuilder to(SynchronousQueue... clients) {
        return new ActionBuilder<CouchTx>() {
          @Override
          public TerminalBuilder<CouchTx> fire() {
            return new AbstractTerminalBuilder<CouchTx>() {
              @Override
              void toVoid() {
                BlobAntiPatternObject.EXECUTOR_SERVICE.submit(new Runnable() {


                  @Override
                  public void run() {
                    try {
                      BlobAntiPatternObject.sendJson("foo", "");
                    } catch (Exception e) {
                      e.printStackTrace();
                    }

                  }
                });
              }

              @Override
              CouchTx tx() throws Exception {
                return BlobAntiPatternObject.sendJson("foo", "");
              }
            };
          }
        };
      }
    };


  }
}


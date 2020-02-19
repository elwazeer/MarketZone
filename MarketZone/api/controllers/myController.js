//// anaaaaa

var express = require("express");
var router = express.Router();
var bodyParser = require("body-parser");
var db = require("../db");
var url = require("url");
router.use(bodyParser.urlencoded({ extended: true }));
router.use(bodyParser.json());

router.get("/login", function(req, res) {
  console.log("got get login request");
  q = url.parse(req.url, true).query;

  var sql =
    "SELECT * FROM users where UID = '" +
    q.UID +
    "' and Password = '" +
    q.Password +
    "'";
  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) throw err;

    res.json(result);
  });
});

router.get("/changePassword", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get changePassword request");

  var sql =
    "Update users set Password = '" +
    q.Password +
    "' where UID = '" +
    q.UID +
    "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/getPassword", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get getPassword request");

  var sql = "Select Password from users where UID = '" + q.UID + "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/changeEmail", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get changeEmail request");

  var sql =
    "Update users set Email = '" + q.Email + "' where UID = '" + q.UID + "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/changeName", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get changeName request");

  var sql =
    "Update users set FirstName = '" +
    q.FirstName +
    "'" +
    " ,LastName = '" +
    q.LastName +
    "'" +
    " where UID = '" +
    q.UID +
    "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/getAddress", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get getAddress request");

  var sql = "Select Address from users where UID = '" + q.UID + "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/changeNumber", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get changeNumber request");

  var sql =
    "Update users set Phone = '" + q.Phone + "' where UID = '" + q.UID + "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/editAddress", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get editAddress request");

  var sql = " SET FOREIGN_KEY_CHECKS=0;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;

    db.mycon.query(
      "Update users set Address = '" +
        q.Address +
        "' Where UID = '" +
        q.UID +
        "'",
      function(err, result) {
        if (err) throw err;
        res.json(result);
        db.mycon.query("SET FOREIGN_KEY_CHECKS=1;", function(err, result) {
          if (err) throw err;
        });
      }
    );
  });
});

router.get("/getOrders", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get getOrders request");

  var sql = "Select * from orders where UID = '" + q.UID + "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});
router.get("/getOrderlatest", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get getOrders request");

  var sql =
    "Select OrderId from orders where UID = '" +
    q.UID +
    "' order by ts DESC limit 1";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/getOrderReorder", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get getOrders request");

  var sql =
    "Select OrderId from orders where UID = '" +
    q.UID +
    "' order by ts DESC limit 1";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/getOrdersAdmin", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get getOrders request");

  var sql = "Select * from orders";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;

    res.json(result);
  });
});

router.get("/updateOrder", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got get updateOrders request");

  var sql =
    "Update orders set Status = " + q.Status + " where OrderId = " + q.OrderId;

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) {
      res.send(err);
    } else {
      res.json(result);
    }
  });
});

router.post("/addOrder", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got post Add Order request");

  var q = {
    UID: req.body.UID,
    Status: req.body.Status,
    StreetAdd: req.body.StreetAdd,
    PaymentMethod: req.body.PaymentMethod
  };

  var sql =
    "Insert into orders (UID, StreetAdd, PaymentMethod, Status) Values ( '" +
    q.UID +
    "' , '" +
    q.StreetAdd +
    "' , '" +
    q.PaymentMethod +
    "' , '" +
    q.Status +
    "')";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) {
      res.send(err);
    } else {
      res.json(result);
    }
  });
});

///////////////////////////////

//// updates after ////////////

///////////////////////////////

router.post("/AddCart", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got post Add to Cart request");

  var q = {
    UID: req.body.UID,
    PID: req.body.PID
  };

  var sql = " SET FOREIGN_KEY_CHECKS=0;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;

    db.mycon.query(
      "Insert into cart (UID,PID, Quantity, OrderId) Values ('" +
        q.UID +
        "' , '" +
        q.PID +
        "' , 1 ,0)",

      function(err, result) {
        if (err) {
          console.log(err);
          db.mycon.query(
            "Update cart set Quantity = 2 " +
              " where UID = " +
              q.UID +
              " AND PID = " +
              q.PID +
              " AND OrderId = 0",

            function(err, result) {
              if (err) {
                console.log(err);
              }
            }
          );
        }

        res.json(result);
        db.mycon.query("SET FOREIGN_KEY_CHECKS=1;", function(err, result) {
          if (err) throw err;
        });
      }
    );
  });
});

router.delete("/DeleteItem", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got delete item from Cart request");

  var q = {
    UID: req.body.UID,
    PID: req.body.PID
  };

  var sql = " SET FOREIGN_KEY_CHECKS=0;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;

    db.mycon.query(
      "Delete from cart where PID = '" +
        q.PID +
        "' AND UID = '" +
        q.UID +
        "' AND OrderId = 0 ",
      function(err, result) {
        if (err) throw err;
        res.json(result);
        db.mycon.query("SET FOREIGN_KEY_CHECKS=1;", function(err, result) {
          if (err) throw err;
        });
      }
    );
  });
});

router.get("/updateQty", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got update item quantity in Cart request");

  var sql = " SET FOREIGN_KEY_CHECKS=0;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;

    db.mycon.query(
      "Update cart set Quantity = " +
        q.Quantity +
        " where UID = " +
        q.UID +
        " AND PID = " +
        q.PID +
        " AND OrderId = 0",
      function(err, result) {
        if (err) throw err;
        res.json(result);
        db.mycon.query("SET FOREIGN_KEY_CHECKS=1;", function(err, result) {
          if (err) throw err;
        });
      }
    );
  });
});

router.get("/getNotifications", function(req, res) {
  console.log("got GET getnotifications request");
  q = url.parse(req.url, true).query;

  var sql = "SELECT * FROM notifications where UID = " + q.UID;

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/getProduct", function(req, res) {
  console.log("got GET getProduct request");
  q = url.parse(req.url, true).query;

  var sql = "SELECT * FROM product where PID = " + q.PID;

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/getProducts", function(req, res) {
  console.log("got GET getProducts request");
  var sql = "SELECT * FROM product";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/getProductsAdmin", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got GET getProducts Admin request");
  var sql = "SELECT PID FROM cart where OrderId =  '" + q.OrderId + "'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/getProductsCart", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got GET getProducts Cart request");
  var sql = "SELECT * FROM cart where UID =  '" + q.UID + "' AND OrderId = '0'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/search", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got GET search request");
  var sql = "SELECT * FROM product WHERE Name LIKE '%";
  sql =
    sql +
    q.keyword +
    "%' OR Category LIKE '%" +
    q.keyword +
    "%' OR Brand LIKE '%" +
    q.keyword +
    "%'";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/getCategories", function(req, res) {
  console.log("got GET getcategories request");
  var sql = "SELECT DISTINCT Category FROM Product ORDER BY Category";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/getBrands", function(req, res) {
  console.log("got GET getBrands request");
  var sql = "SELECT DISTINCT Brand FROM Product ORDER BY Brand";

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.get("/filterSearch", function(req, res) {
  console.log("got GET filterSearch request");
  q = url.parse(req.url, true).query;
  var sql =
    "SELECT * FROM Product WHERE Category = " +
    q.category +
    " AND Brand = " +
    q.brand +
    " AND PRICE<=" +
    q.price +
    " ORDER BY " +
    q.order;

  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));

    if (err) if (err) throw err;

    res.json(result);
  });
});

router.post("/createUser", function(req, res) {
  console.log("got POST createUser request");
  q = url.parse(req.url, true).query;
  var q = {
    username: req.body.username,
    firstname: req.body.firstname,
    lastname: req.body.lastname,
    phone: req.body.phone,
    email: req.body.email,
    pass: req.body.pass
  };

  let sql =
    'INSERT INTO users(LastName,FirstName,UID,Phone,Email,Password,type) VALUES("';
  sql =
    sql +
    q.lastname +
    '","' +
    q.firstname +
    '","' +
    q.username +
    '","' +
    q.phone +
    '","' +
    q.email +
    '","' +
    q.pass +
    '",0);';
  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;
    res.json(result);
    console.log("1 user added");
  });
});

router.post("/addNotification", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got POST addNotification request");
  var q = {
    msg: req.body.msg,
    UID: req.body.UID
  };

  var sql = " SET FOREIGN_KEY_CHECKS=0;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;
    sql = 'INSERT INTO notifications(msg,UID) VALUES("';
    sql = sql + q.msg + '","' + q.UID + '");';
    db.mycon.query(sql, function(err, result) {
      if (err) throw err;
      res.json(result);
      db.mycon.query("SET FOREIGN_KEY_CHECKS=1;", function(err, result) {
        if (err) throw err;
      });
    });
  });
});

router.post("/deleteNotification", function(req, res) {
  console.log("got POST deleteNotification request");
  var q = {
    nid: req.body.nid,
    uid: req.body.uid
  };

  let sql = 'DELETE FROM notifications WHERE NID = "';
  sql = sql + q.nid + '" AND UID = "' + q.uid + '";';
  db.mycon.query(sql, function(err, result) {
    console.log("Result: " + JSON.stringify(result));
    if (err) throw err;
    res.json(result);
    console.log("1 notification deleted");
  });
});

router.get("/CartReOrder", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got post Add to Cart request");

  var sql = " SET FOREIGN_KEY_CHECKS=0;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;

    db.mycon.query(
      "Update cart set OrderId = '" +
        q.OrderId +
        "' where UID = '" +
        q.UID +
        "' AND OrderId = '" +
        q.old +
        "'",
      function(err, result) {
        if (err) {
          console.log(err);
        }

        res.json(result);
        db.mycon.query("SET FOREIGN_KEY_CHECKS=1;", function(err, result) {
          if (err) throw err;
        });
      }
    );
  });
});

router.get("/CartOrder", function(req, res) {
  q = url.parse(req.url, true).query;
  console.log("got order in Cart request");

  var sql =
    "Update cart set OrderId = '" +
    q.OrderId +
    "' where UID = '" +
    q.UID +
    "' and OrderId = 0 ;";
  db.mycon.query(sql, function(err, result) {
    if (err) throw err;

    res.json(result);
  });
});

module.exports = router;

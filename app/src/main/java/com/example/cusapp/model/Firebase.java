package com.example.cusapp.model;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cusapp.model.Car;
import com.example.cusapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Firebase {
    FirebaseFirestore mfirestore;
    FirebaseAuth mfirebaseAuth;
    FirebaseUser mfirebaseUser;
    FirebaseStorage mfirebaseStorage;
    StorageReference mstorageRef;
    Context mcontext;
    public Firebase(Context context) {
        mfirestore = FirebaseFirestore.getInstance();
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseStorage = FirebaseStorage.getInstance();
        mstorageRef = mfirebaseStorage.getReference();
        this.mcontext = context;
    }
    ///////
    public interface LoginCallback {
        void onCallback(boolean isSuccess, String userId);
    }
    public interface FirebaseCallback<T> {
        void onCallback(ArrayList<T> list);
    }
    public interface UserCallback {
        void onCallback(User user);
    }
    public interface UpdateUserCallback {
        void onCallback(boolean isSuccess);
    }
    public interface SaveOrderCallback {
        void onCallback(boolean isSuccess);
    }
    public interface SaveReasonsCancelCallback {
        void onCallback(boolean isSuccess);
    }
    public interface UpdateStatusOrderCallback {
        void onCallback(boolean isSuccess);
    }
    public interface getAllHistoryOrdersCallback {
        void onCallback(ArrayList<Order> ordersList);
    }
    //////////
    public void getAllXe(FirebaseCallback<Car> callback) {
        ArrayList<Car> carList = new ArrayList<>();
        mfirestore.collection("cars")
                .whereEqualTo("statuscar", "Còn Trống")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Car car = new Car(document.getId(),
                                    document.getString("namecar"),
                                    document.getDouble("pricecar"),
                                    document.getString("typecar"),
                                    document.getDouble("seats"),
                                    document.getString("statuscar"),
                                    document.getString("descriptioncar"),
                                    document.getString("image1"),
                                    document.getString("image2"),
                                    document.getString("image3"));
                            carList.add(car);
                        }
                        callback.onCallback(carList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
    public void getAllXeSoTuDong(FirebaseCallback<Car> callback) {
        ArrayList<Car> carList = new ArrayList<>();
        mfirestore.collection("cars")
                .whereEqualTo("typecar", "Số tự động")
                .whereEqualTo("statuscar", "Còn Trống")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Car car = new Car(document.getId(),
                                    document.getString("namecar"),
                                    document.getDouble("pricecar"),
                                    document.getString("typecar"),
                                    document.getDouble("seats"),
                                    document.getString("statuscar"),
                                    document.getString("descriptioncar"),
                                    document.getString("image1"),
                                    document.getString("image2"),
                                    document.getString("image3"));
                            carList.add(car);
                        }
                        callback.onCallback(carList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
    public void getAllXeSoSan(FirebaseCallback<Car> callback) {
        ArrayList<Car> carList = new ArrayList<>();
        mfirestore.collection("cars")
                .whereEqualTo("typecar", "Số sàn")
                .whereEqualTo("statuscar", "Còn Trống")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Car car = new Car(document.getId(),
                                    document.getString("namecar"),
                                    document.getDouble("pricecar"),
                                    document.getString("typecar"),
                                    document.getDouble("seats"),
                                    document.getString("statuscar"),
                                    document.getString("descriptioncar"),
                                    document.getString("image1"),
                                    document.getString("image2"),
                                    document.getString("image3"));
                            carList.add(car);
                        }
                        callback.onCallback(carList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
    public void getInforUserByUserId(String userId, UserCallback callback) {
        mfirestore.collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = new User(
                                    document.getId(),
                                    document.getString("username"),
                                    document.getString("numberphone"),
                                    document.getString("address"),
                                    document.getString("giaypheplaixe"),
                                    document.getString("dateofbirth"),
                                    document.getString("gioitinh"),
                                    document.getString("image")
                            );
                            callback.onCallback(user);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get in4 user failed with ", task.getException());
                    }
                });
    }
    public void loginWithPhoneNumber(String phoneNumber, String password, LoginCallback callback) {
        mfirestore.collection("users")
                .whereEqualTo("numberphone", phoneNumber)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // User found, login successful
                        DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);
                        String userId = userDoc.getId();
                        callback.onCallback(true, userId);
                    } else {
                        // User not found or login failed
                        callback.onCallback(false, null);
                    }
                });
    }
    public void updateUserInfo(String userID, String username, String address, String giaypheplaixe, String dateofbirth, String gioitinh, UpdateUserCallback callback) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("address", address);
        user.put("giaypheplaixe", giaypheplaixe);
        user.put("dateofbirth", dateofbirth);
        user.put("gioitinh", gioitinh);

        mfirestore.collection("users").document(userID).update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onCallback(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
    }
    public void updateUserInfoAndImage(String userID, String username, String address, String giaypheplaixe, String dateofbirth, String gioitinh,Uri imageUri, UpdateUserCallback callback) {
        final StorageReference imgRef = mstorageRef.child("images/" + UUID.randomUUID().toString());
        imgRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("username", username);
                                user.put("address", address);
                                user.put("giaypheplaixe", giaypheplaixe);
                                user.put("dateofbirth", dateofbirth);
                                user.put("gioitinh", gioitinh);
                                user.put("image", uri.toString());

                                mfirestore.collection("users").document(userID).update(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                callback.onCallback(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                callback.onCallback(false);
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onCallback(false);
                    }
                });
    }
    public void saveOrder(Order order, SaveOrderCallback callback) {
        Map<String, Object> morder = new HashMap<>();
        morder.put("userID", order.getUserID());
        morder.put("orderDate", order.getOrderDate());
        morder.put("orderStatus", order.getOrderStatus());
        morder.put("rentDate", order.getRentDate());
        morder.put("returnDate", order.getReturnDate());
        morder.put("renterName", order.getRenterName());
        morder.put("renterPhone", order.getRenterPhone());
        morder.put("carID", order.getCarID());
        morder.put("carName", order.getCarName());
        morder.put("carPrice", order.getCarPrice());
        morder.put("carType", order.getCarType());
        morder.put("paymentMethod", order.getPaymentMethod());
        morder.put("totalprice", order.getTotalprice());
        mfirestore.collection("orders")
                .add(morder)
                .addOnSuccessListener(documentReference -> {
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving order: " + e.getMessage());
                    callback.onCallback(false);
                });
    }
    public void getAllHistoryOrders(String userID, getAllHistoryOrdersCallback callback) {
        mfirestore.collection("orders")
                .whereEqualTo("userID", userID)
                .whereIn("orderStatus", Arrays.asList("Đã hoàn thành", "Đơn hàng đã bị hủy"))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Order> ordersList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = new Order(document.getId(),
                                    document.getString("userID"),
                                    document.getString("orderDate"),
                                    document.getString("orderStatus"),
                                    document.getString("rentDate"),
                                    document.getString("returnDate"),
                                    document.getString("renterName"),
                                    document.getString("renterPhone"),
                                    document.getString("carID"),
                                    document.getString("carName"),
                                    document.getDouble("carPrice"),
                                    document.getString("carType"),
                                    document.getString("paymentMethod"),
                                    document.getDouble("totalprice"));
                            ordersList.add(order);
                        }
                        callback.onCallback(ordersList);
                    } else {
                        callback.onCallback(null);
                    }
                });
    }
    public void getAllOrders(String userID, getAllHistoryOrdersCallback callback) {
        mfirestore.collection("orders")
                .whereEqualTo("userID", userID)
                .whereIn("orderStatus", Arrays.asList("Đang chờ xác nhận", "Đang thuê"))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Order> ordersList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Order order = new Order(document.getId(),
                                    document.getString("userID"),
                                    document.getString("orderDate"),
                                    document.getString("orderStatus"),
                                    document.getString("rentDate"),
                                    document.getString("returnDate"),
                                    document.getString("renterName"),
                                    document.getString("renterPhone"),
                                    document.getString("carID"),
                                    document.getString("carName"),
                                    document.getDouble("carPrice"),
                                    document.getString("carType"),
                                    document.getString("paymentMethod"),
                                    document.getDouble("totalprice"));
                            ordersList.add(order);
                        }
                        callback.onCallback(ordersList);
                    } else {
                        callback.onCallback(null);
                    }
                });
    }
    public void saveReasonsCancel(String orderID,String reasonscancel, SaveReasonsCancelCallback callback) {
        Map<String, Object> morder = new HashMap<>();
        morder.put("reasonscancel", reasonscancel);
        mfirestore.collection("orders")
                .document(orderID)
                .update(morder)
                .addOnSuccessListener(documentReference -> {
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving ReasonsCancel: " + e.getMessage());
                    callback.onCallback(false);
                });
    }
    public void updateStatusOrder(String orderID,String status, UpdateStatusOrderCallback callback) {
        Map<String, Object> morder = new HashMap<>();
        morder.put("orderStatus", status);
        mfirestore.collection("orders")
                .document(orderID)
                .update(morder)
                .addOnSuccessListener(documentReference -> {
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving ReasonsCancel: " + e.getMessage());
                    callback.onCallback(false);
                });
    }
    public void updateStatusCar(String carID,String status, UpdateStatusOrderCallback callback) {
        Map<String, Object> room = new HashMap<>();
        room.put("statuscar", status);
        mfirestore.collection("cars")
                .document(carID)
                .update(room)
                .addOnSuccessListener(documentReference -> {
                    callback.onCallback(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving ReasonsCancel: " + e.getMessage());
                    callback.onCallback(false);
                });
    }
}

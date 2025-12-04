package com.terra.terraPizza.Bussines;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terra.terraPizza.DataAcces.*;
import com.terra.terraPizza.Entities.*;
import com.terra.terraPizza.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PizzaService implements IPizzaService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BranchRepository branchRepo;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private OrderMapper orderMapper;



    public ResponseEntity<?> addAddress( AddressRequest request) {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();

        Address address = new Address();
        address.setTitle(request.getTitle());
        address.setFullAddress(request.getFullAddress());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setUser(user);

        addressRepository.save(address);

        return ResponseEntity.ok("Adres başarıyla eklendi");
    }

    public ResponseEntity<?> getUserAddresses() {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();
        List<Address> addresses = addressRepository.findByUser(user);

        return ResponseEntity.ok(addresses);
    }

    public ResponseEntity<?> deleteAddress(Long id) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres bulunamadı");
        }

        Address address = optionalAddress.get();
        String email = getCurrentEmail();
        if (!address.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu adrese erişiminiz yok");
        }

        addressRepository.delete(address);
        return ResponseEntity.ok("Adres silindi");
    }

    public ResponseEntity<?> updateAddress(Long id,AddressRequest request) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Adres bulunamadı");
        }

        Address address = optionalAddress.get();
        String email = getCurrentEmail();
        if (!address.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu adrese erişiminiz yok");
        }

        address.setTitle(request.getTitle());
        address.setFullAddress(request.getFullAddress());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());

        addressRepository.save(address);
        return ResponseEntity.ok("Adres güncellendi");
    }

    public ResponseEntity<?> setDefaultAddress(Long id) {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();

        List<Address> addresses = addressRepository.findByUser(user);
        addresses.forEach(a -> {
            a.setDefault(false);
            addressRepository.save(a);
        });

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı"));

        if (!address.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bu adrese erişiminiz yok");
        }

        address.setDefault(true);
        addressRepository.save(address);

        return ResponseEntity.ok("Varsayılan adres olarak ayarlandı");
    }

    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchRepo.findAll());
    }


    public ResponseEntity<Branch> createBranch(Branch branch) {
        Branch saved = branchRepo.save(branch);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    public ResponseEntity<?> updateBranch(Long id, Branch updatedBranch) {
        return branchRepo.findById(id)
                .map(branch -> {
                    branch.setCity(updatedBranch.getCity());
                    branch.setDistrict(updatedBranch.getDistrict());
                    branch.setNeighborhood(updatedBranch.getNeighborhood());
                    branch.setName(updatedBranch.getName());
                    branch.setOpen(updatedBranch.isOpen());
                    branch.setHours(updatedBranch.getHours());
                    branch.setLatitude(updatedBranch.getLatitude());
                    branch.setLongitude(updatedBranch.getLongitude());
                    branchRepo.save(branch);
                    return ResponseEntity.ok(branch);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    public ResponseEntity<?> deleteBranch(Long id) {
        if (!branchRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Şube bulunamadı");
        }
        branchRepo.deleteById(id);
        return ResponseEntity.ok("Şube silindi");
    }


    public ResponseEntity<List<Branch>> searchBranches(
            String city,
            String district,
            String neighborhood
    ) {
        List<Branch> results = branchRepo.findByCityAndDistrictAndNeighborhood(city, district, neighborhood);
        return ResponseEntity.ok(results);
    }


    public ResponseEntity<Map<String, List<Map<String, String>>>> getFilterOptions() {
        List<String> cities = branchRepo.findAll().stream()
                .map(Branch::getCity)
                .distinct()
                .toList();

        List<Map<String, String>> districts = branchRepo.findAll().stream()
                .map(b -> Map.<String, String>of(
                        "name", b.getDistrict(),
                        "city", b.getCity()
                ))
                .distinct()
                .toList();

        List<Map<String, String>> neighborhoods = branchRepo.findAll().stream()
                .map(b -> Map.<String, String>of(
                        "name", b.getNeighborhood(),
                        "district", b.getDistrict()
                ))
                .distinct()
                .toList();

        Map<String, List<Map<String, String>>> result = Map.of(
                "cities", cities.stream().map(c -> Map.<String,String>of("name", c)).toList(), // city'yi de Map yapıyoruz
                "districts", districts,
                "neighborhoods", neighborhoods
        );

        return ResponseEntity.ok(result);
    }


    public ResponseEntity<List<Branch>> getAllBranches2() {
        return ResponseEntity.ok(branchRepo.findAll());
    }


    public ResponseEntity<?> addFavorite(FavoriteRequest request) {
        String email = getCurrentEmail();
        // User user = userRepository.findByEmail(email).orElseThrow();
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(request.getProductId()).orElseThrow();

        //if (favoriteRepository.existsByUserAndProduct(user, product)) {
        //   return ResponseEntity.status(HttpStatus.CONFLICT).body("Zaten favorilerde");
        // }

        Optional<Favorite> existing = favoriteRepository.findByUserAndProduct(user, product);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Zaten favorilerde");
        }


        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);

        return ResponseEntity.ok("Favoriye eklendi");
    }


    public ResponseEntity<?> removeFavorite(Long productId) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email);
        Product product = productRepository.findById(productId).orElseThrow();

        favoriteRepository.deleteByUserAndProduct(user, product);
        return ResponseEntity.ok("Favoriden çıkarıldı");
    }


    public List<OrderResponse> getOrders() {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email);

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderDate(),
                        order.getTotalPrice(),
                        order.getStatus(),
                        order.getItems().stream()
                                .map(OrderItemResponse::new)
                                .toList()
                ))
                .toList();
    }


    public ResponseEntity<?> createOrder(OrderRequest request) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email);

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Hazırlanıyor");
        order.setPaymentStatus(2); // string veya int yapına göre
        order.setTotalPrice(request.getTotalPrice());

        List<OrderItem> items = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(); // JSON dönüşümü için

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ürün bulunamadı"));

            OrderItem item = new OrderItem();
            item.setOrder(order); // parent ile ilişkilendir
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setDescription(product.getDescription());

            BigDecimal totalPrice = itemReq.getTotalPrice() != null
                    ? itemReq.getTotalPrice()
                    : product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            item.setTotalPrice(totalPrice);

            // 🔹 Toppings JSON olarak kaydediliyor
            try {
                if (itemReq.getToppings() != null) {
                    item.setToppings(mapper.writeValueAsString(itemReq.getToppings()));
                } else {
                    item.setToppings("[]");
                }
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Toppings JSON dönüşümü hatası");
            }

            // 🔹 Extra Mozzarella alanı
            item.setExtraMozzarella(itemReq.isExtraMozzarella());

            items.add(item);
        }

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", savedOrder.getId());
        response.put("status", "created");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public List<PizzaDto> getAllPizza(){
        return pizzaRepository.findAll()
                .stream().map(PizzaDto::new)
                .collect(Collectors.toList());
    }


    public List<Product> getByCategory(String category) {
        return productRepository.findByCategory(category);
    }


    public ResponseEntity<?> getProfile() {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        String email = getCurrentEmail();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());
        user.setSms(request.isSms());
        user.setEmailPermission(request.isEmailPermission());
        user.setPhonePermission(request.isPhonePermission());

        userRepository.save(user);

        return ResponseEntity.ok("Profil güncellendi");
    }


    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        String email = getCurrentEmail(); // JWT'den email al
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        User user = optionalUser.get();

        // Eski şifreyi kontrol et
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eski şifre hatalı");
        }

        // Yeni şifreyi encode edip kaydet
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("Şifre başarıyla değiştirildi");
    }



    private String getCurrentEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}

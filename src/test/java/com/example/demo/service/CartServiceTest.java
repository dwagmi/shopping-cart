package com.example.demo.service;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    private CartService cartService;
    private List<Cart> fakeCarts;
    private Cart fakeEmptyCart;
    private Product fakeProduct;

    @Mock
    private CartRepository mockCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeEach
    public void setUp() {
        cartService = new CartService(mockCartRepository, cartItemRepository);
        fakeProduct = new Product("sku-001", "Test Product", 34.99, 3);
        fakeCarts = new ArrayList<>();
        fakeEmptyCart = new Cart();
        IntStream.range(0, 10).forEach(i -> fakeCarts.add(new Cart()));
    }

    @Test
    public void testGetAllCarts() {
        when(mockCartRepository.findAllByOrderByIdAsc()).thenReturn(fakeCarts);

        List<Cart> carts = cartService.getAllCarts();

        verify(mockCartRepository, times(1)).findAllByOrderByIdAsc();
        assertEquals(fakeCarts, carts);
    }

    @Test
    @Disabled
    @DisplayName("Adding a product to an empty cart should update the cart with the product")
    public void testAddOneProductToEmptyCart() {
        int quantity = 1;

        cartService.addProduct(fakeEmptyCart, fakeProduct, quantity);

        assertEquals(1, fakeEmptyCart.totalItems());
        assertEquals(fakeProduct, fakeEmptyCart.getCartItems().get(0).getProduct());
    }
}

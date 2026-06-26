// ============================================
//  BookNest - Main JavaScript Utilities
// ============================================

const API_BASE = 'http://localhost:8080/bookstore/api';

// ---- Auth Helpers ----
function getUser() {
    const data = localStorage.getItem('bookstore_user');
    return data ? JSON.parse(data) : null;
}

function logout() {
    localStorage.removeItem('bookstore_user');
    window.location = 'index.html';
}

function updateNavAuth() {
    const user = getUser();
    const authLinks = document.getElementById('authLinks');
    const userLinks = document.getElementById('userLinks');
    const welcomeUser = document.getElementById('welcomeUser');

    if (user) {
        if (authLinks) authLinks.style.display = 'none';
        if (userLinks) userLinks.style.display = 'flex';
        if (welcomeUser) welcomeUser.textContent = 'Hi, ' + user.fullName;
    } else {
        if (authLinks) authLinks.style.display = 'flex';
        if (userLinks) userLinks.style.display = 'none';
    }
}

// ---- Cart Helpers ----
function getCart() {
    const data = localStorage.getItem('bookstore_cart');
    return data ? JSON.parse(data) : [];
}

function saveCart(cart) {
    localStorage.setItem('bookstore_cart', JSON.stringify(cart));
}

function addToCart(book) {
    let cart = getCart();
    const existing = cart.find(i => i.id === book.id);
    if (existing) {
        existing.quantity++;
    } else {
        cart.push({ ...book, quantity: 1 });
    }
    saveCart(cart);
    updateCartBadge();
    showToast(`"${book.title}" added to cart!`);
}

function updateCartBadge() {
    const badge = document.getElementById('cartBadge');
    if (badge) {
        const count = getCart().reduce((sum, i) => sum + i.quantity, 0);
        badge.textContent = count;
        badge.style.display = count > 0 ? 'inline-block' : 'none';
    }
}

// ---- Book Card Template ----
function createBookCard(book) {
    const coverColors = ['#f9a825','#e53935','#43a047','#1e88e5','#8e24aa','#00897b'];
    const color = coverColors[book.id % coverColors.length];
    const imgHtml = book.imageUrl
        ? `<img src="${book.imageUrl}" alt="${book.title}" onerror="this.style.display='none';this.nextElementSibling.style.display='flex'"/><div class="book-cover-placeholder" style="background:${color};display:none"><i class="fas fa-book fa-2x"></i></div>`
        : `<div class="book-cover-placeholder" style="background:${color}"><i class="fas fa-book fa-2x"></i></div>`;

    return `
    <div class="book-card" onclick="openBookDetail(${book.id})">
        <div class="book-cover">
            ${imgHtml}
            <div class="book-overlay">
                <button class="btn-overlay" onclick="event.stopPropagation(); quickAddToCart(${book.id})">
                    <i class="fas fa-cart-plus"></i> Add to Cart
                </button>
            </div>
        </div>
        <div class="book-info">
            <h4 class="book-title">${book.title}</h4>
            <p class="book-author">by ${book.author}</p>
            <div class="book-footer">
                <span class="book-price">₹${book.price.toFixed(2)}</span>
                <span class="book-stock ${book.stock < 5 ? 'low-stock' : ''}">
                    ${book.stock > 0 ? (book.stock < 5 ? 'Only ' + book.stock + ' left' : 'In Stock') : 'Out of Stock'}
                </span>
            </div>
        </div>
    </div>`;
}

// ---- Quick Add from Grid ----
function quickAddToCart(bookId) {
    fetch(API_BASE + '/books/' + bookId)
        .then(r => r.json())
        .then(book => {
            if (book && book.stock > 0) {
                addToCart(book);
            } else {
                showToast('This book is out of stock.');
            }
        });
}

// ---- Book Detail Modal ----
function openBookDetail(bookId) {
    fetch(API_BASE + '/books/' + bookId)
        .then(r => r.json())
        .then(book => {
            if (!book) return;
            const modal = document.getElementById('bookModal');
            if (!modal) return;

            document.getElementById('modalBody').innerHTML = `
                <div class="modal-book">
                    <div class="modal-cover">
                        ${book.imageUrl
                            ? `<img src="${book.imageUrl}" alt="${book.title}"/>`
                            : `<div class="modal-placeholder"><i class="fas fa-book fa-4x"></i></div>`}
                    </div>
                    <div class="modal-details">
                        <span class="modal-category">${book.categoryName || 'General'}</span>
                        <h2>${book.title}</h2>
                        <p class="modal-author">by <strong>${book.author}</strong></p>
                        ${book.publisher ? `<p><i class="fas fa-building"></i> ${book.publisher} (${book.publishYear})</p>` : ''}
                        ${book.isbn ? `<p><i class="fas fa-barcode"></i> ISBN: ${book.isbn}</p>` : ''}
                        <p class="modal-desc">${book.description || 'No description available.'}</p>
                        <div class="modal-footer">
                            <span class="modal-price">₹${book.price.toFixed(2)}</span>
                            <span class="modal-stock ${book.stock < 5 ? 'low-stock' : ''}">
                                ${book.stock > 0 ? 'In Stock (' + book.stock + ')' : 'Out of Stock'}
                            </span>
                        </div>
                        <button class="btn-primary" onclick="addToCart(${JSON.stringify(book).replace(/"/g,'&quot;')}); closeModal();" ${book.stock === 0 ? 'disabled' : ''}>
                            <i class="fas fa-cart-plus"></i> Add to Cart
                        </button>
                    </div>
                </div>`;
            modal.style.display = 'flex';
        });
}

function closeModal() {
    const modal = document.getElementById('bookModal');
    if (modal) modal.style.display = 'none';
}

window.onclick = function(e) {
    const modal = document.getElementById('bookModal');
    if (modal && e.target === modal) modal.style.display = 'none';
};

// ---- Toast ----
function showToast(message, duration = 3000) {
    const toast = document.getElementById('toast');
    if (!toast) return;
    toast.textContent = message;
    toast.classList.add('show');
    setTimeout(() => toast.classList.remove('show'), duration);
}

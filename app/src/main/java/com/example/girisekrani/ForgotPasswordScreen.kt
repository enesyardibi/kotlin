package com.example.girisekrani

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.girisekrani.core.util.isValidPassword
import com.example.girisekrani.core.util.isValidPhoneNumber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(onPasswordResetSuccess: () -> Unit = {}, onBackToLogin: () -> Unit = {}) {
    val context = LocalContext.current
    val vm: ForgotPasswordViewModel = viewModel(factory = ForgotPasswordViewModelFactory(context))
    val uiState by vm.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF667eea), Color(0xFF764ba2)))
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Başlık Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), 
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackToLogin) {
                            Icon(Icons.Default.ArrowBack, "Geri", tint = Color(0xFF2C3E50))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Şifremi Unuttum", 
                            fontSize = 24.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Color(0xFF2C3E50),
                            modifier = Modifier.weight(1f),
                            softWrap = true
                        )
                    }
                    Text(
                        text = if (!uiState.showNewPasswordFields) "Kimlik doğrulama" else "Yeni şifre oluşturun",
                        fontSize = 16.sp, 
                        color = Color(0xFF7F8C8D),
                        textAlign = TextAlign.Center,
                        softWrap = true
                    )
                }
            }

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    
                    if (!uiState.showNewPasswordFields) {
                        // Kimlik Doğrulama Aşaması
                        Text(
                            text = "1. Adım: Kimlik Doğrulama", 
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            softWrap = true,
                            overflow = TextOverflow.Visible,
                            maxLines = 3,
                            lineHeight = 20.sp
                        )
                        
                        OutlinedTextField(
                            value = uiState.phoneNumber,
                            onValueChange = { 
                                vm.updatePhoneNumber(it)
                            },
                            label = { Text("Telefon Numarası") },
                            placeholder = { Text("5XX XXX XX XX") },
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            maxLines = 1
                        )

                        OutlinedTextField(
                            value = uiState.fullName,
                            onValueChange = { 
                                vm.updateFullName(it)
                            },
                            label = { Text("Ad Soyad") },
                            placeholder = { Text("Kayıt sırasındaki ad soyad") },
                            leadingIcon = { Icon(Icons.Default.Person, null) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            maxLines = 1
                        )

                        Button(
                            onClick = {
                                vm.verifyIdentity()
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = "Kimliği Doğrula",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                    } else {
                        // Şifre Değiştirme Aşaması
                        Text(
                            text = "2. Adım: Yeni Şifre", 
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth(),
                            softWrap = true,
                            overflow = TextOverflow.Visible,
                            maxLines = 2
                        )
                        
                        OutlinedTextField(
                            value = uiState.newPassword,
                            onValueChange = { 
                                vm.updateNewPassword(it)
                            },
                            label = { Text("Yeni Şifre") },
                            placeholder = { Text("Yeni Şifre") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            trailingIcon = {
                                IconButton(onClick = { 
                                vm.togglePasswordVisibility()
                                }) {
                                    Icon(if (uiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                                }
                            },
                            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            maxLines = 1
                        )
                        
                        // Şifre gereksinimleri uyarısı
                        Text(
                            text = "• En az 6 karakter\n• En az 1 Büyük harf ve küçük harf içermeli",
                            color = Color(0xFF7F8C8D),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )

                        OutlinedTextField(
                            value = uiState.confirmPassword,
                            onValueChange = { 
                                vm.updateConfirmPassword(it)
                            },
                            label = { Text("Yeni Şifre Tekrar") },
                            placeholder = { Text("Yeni Şifre Tekrar") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            trailingIcon = {
                                IconButton(onClick = { 
                                vm.toggleConfirmPasswordVisibility()
                                }) {
                                    Icon(if (uiState.confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                                }
                            },
                            visualTransformation = if (uiState.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            maxLines = 1
                        )

                        Button(
                            onClick = {
                                vm.updatePassword(onPasswordResetSuccess)
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text(
                                    text = "Şifreyi Güncelle",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Mesajlar
                    if (uiState.successMessage.isNotEmpty()) {
                        Text(
                            text = uiState.successMessage, 
                            color = Color(0xFF4CAF50), 
                            textAlign = TextAlign.Center, 
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 14.sp,
                            softWrap = true
                        )
                    }
                    if (uiState.errorMessage.isNotEmpty()) {
                        Text(
                            text = uiState.errorMessage, 
                            color = Color.Red, 
                            textAlign = TextAlign.Center, 
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 14.sp,
                            softWrap = true
                        )
                    }

                    // Geri dön
                    if (!uiState.showNewPasswordFields) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text("Hatırladınız mı? ", color = Color(0xFF7F8C8D), fontSize = 14.sp)
                            Text(
                                "Giriş Yapın", 
                                color = Color(0xFF667eea), 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { onBackToLogin() }
                            )
                        }
                    }
                }
            }
        }
    }
}
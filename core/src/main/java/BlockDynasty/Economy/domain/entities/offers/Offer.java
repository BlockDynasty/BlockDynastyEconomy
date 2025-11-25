/**
 * Copyright 2025 Federico Barrionuevo "@federkone"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package BlockDynasty.Economy.domain.entities.offers;

import BlockDynasty.Economy.domain.entities.account.Player;
import BlockDynasty.Economy.domain.entities.currency.Currency;
import BlockDynasty.Economy.domain.entities.currency.ICurrency;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Offer implements IOffer {
    private Player vendedor;
    private Player comprador;
    private BigDecimal cantidad;  //puede ser de tipo balance, compuesto por currency y cantidad.
    private ICurrency tipoCantidad;
    private BigDecimal monto;
    private ICurrency tipoMonto;

    public Offer(Player vendedor, Player comprador, BigDecimal cantidad, BigDecimal monto, ICurrency tipoCantidad, ICurrency tipoMonto) {
        this.vendedor = vendedor;
        this.comprador = comprador;
        this.cantidad = cantidad;
        this.tipoCantidad = tipoCantidad;
        this.monto = monto;
        this.tipoMonto = tipoMonto;
    }

    public Player getVendedor() {
        return this.vendedor;
    }

    public Player getComprador() {
        return this.comprador;
    }

    //cantidad de moneda ofertada
    public BigDecimal getCantidad() {
        return this.cantidad;
    }

    //tipo de moneda ofertada
    public ICurrency getTipoCantidad() {
        return this.tipoCantidad;
    }

    //monto a pagar
    public BigDecimal getMonto() {
        return this.monto;
    }
    //tipo de moneda monto a pagar
    public ICurrency getTipoMonto() {
        return this.tipoMonto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.vendedor.getUuid().toString(), this.comprador.getUuid().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(vendedor.getUuid().toString(), offer.vendedor.getUuid().toString()) &&
                Objects.equals(comprador.getUuid().toString(), offer.comprador.getUuid().toString());
    }

}

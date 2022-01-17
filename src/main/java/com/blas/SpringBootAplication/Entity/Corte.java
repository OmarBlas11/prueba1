package com.blas.SpringBootAplication.Entity;


import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Corte {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO, generator="native")
	@GenericGenerator(name="native",strategy="native")
	private Long id;
	
	@Column(unique = true)
	@NotBlank
	private String CodigoCorte;
	
	@Column(unique = true)
	@NotBlank
	private double CostoCorte;
	
	@Column(unique = true)
	@NotBlank
	private Date FechaCorte;
	
	@Size(min=1)
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name="users_corte"
		,joinColumns=@JoinColumn(name="user_id")
		,inverseJoinColumns=@JoinColumn(name="corte_id"))
	private Set<User> users;
	
	public Corte() {
		super();
	}

	@Override
	public String toString() {
		return "Corte [id=" + id + ", CodigoCorte=" + CodigoCorte + ", CostoCorte=" + CostoCorte + ", FechaCorte="
				+ FechaCorte + ", users=" + users + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoCorte() {
		return CodigoCorte;
	}

	public void setCodigoCorte(String codigoCorte) {
		CodigoCorte = codigoCorte;
	}

	public double getCostoCorte() {
		return CostoCorte;
	}

	public void setCostoCorte(double costoCorte) {
		CostoCorte = costoCorte;
	}

	public Date getFechaCorte() {
		return FechaCorte;
	}

	public void setFechaCorte(Date fechaCorte) {
		FechaCorte = fechaCorte;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((CodigoCorte == null) ? 0 : CodigoCorte.hashCode());
		long temp;
		temp = Double.doubleToLongBits(CostoCorte);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((FechaCorte == null) ? 0 : FechaCorte.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Corte other = (Corte) obj;
		if (CodigoCorte == null) {
			if (other.CodigoCorte != null)
				return false;
		} else if (!CodigoCorte.equals(other.CodigoCorte))
			return false;
		if (Double.doubleToLongBits(CostoCorte) != Double.doubleToLongBits(other.CostoCorte))
			return false;
		if (FechaCorte == null) {
			if (other.FechaCorte != null)
				return false;
		} else if (!FechaCorte.equals(other.FechaCorte))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
	
	//public static void main(String[] args) {
//		Calendar calendario=Calendar.getInstance();
//		int FechaCorte;
//		FechaCorte=calendario.get(Calendar.DATE);
		
		//DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
//        
//        String firstDate1="";
//        firstDate1=dtf2.format(LocalDateTime.now());
//		System.out.print(firstDate1);
		
	//}
//	public static void main(String[] args) throws ParseException {
//	      
//		 Date Fecha= Calendar.getInstance().getTime();
//		 SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
//		 String dataFormateada = formato.parse(Fecha); 
//		        System.out.println("la fecha es: "+Fecha);
//		    }
}

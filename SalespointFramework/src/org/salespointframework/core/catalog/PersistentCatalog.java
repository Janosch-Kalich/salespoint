package org.salespointframework.core.catalog;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.salespointframework.core.database.Database;
import org.salespointframework.core.product.PersistentProduct;
import org.salespointframework.core.product.PersistentProduct_;
import org.salespointframework.core.product.ProductIdentifier;
import org.salespointframework.util.ArgumentNullException;
import org.salespointframework.util.Iterables;
import org.salespointframework.util.Objects;

// TODO comment
/**
 * 
 * @author Paul Henke
 * 
 */

public class PersistentCatalog implements Catalog<PersistentProduct>
{
	private final EntityManagerFactory emf = Database.INSTANCE.getEntityManagerFactory();

	/**
	 * Creates a new PersistentCatalog.
	 */
	public PersistentCatalog()
	{

	}

	@Override
	public final void add(PersistentProduct product)
	{
		Objects.requireNonNull(product, "product");
		EntityManager em = emf.createEntityManager();
		em.persist(product);
		beginCommit(em);
	}

	
	/**
	 * Adds multiple {@link PersistentProduct}s to this PersistentCatalog
	 * @param products an {@link Iterable} of {@link PersistentProduct}s or subtypes to be added
	 */
	public final void addAll(Iterable<? extends PersistentProduct> products)
	{
		Objects.requireNonNull(products, "products");
		EntityManager em = emf.createEntityManager();
		for (PersistentProduct product : products)
		{
			em.persist(product);
		}
		beginCommit(em);
	}


	
	@Override
	public final boolean remove(ProductIdentifier productIdentifier)
	{
		// TODO catch exception
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		Object product = em.find(PersistentProduct.class, productIdentifier);
		if(product != null)
		{
			em.remove(product);
			beginCommit(em);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final boolean contains(ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(PersistentProduct.class, productIdentifier) != null;
	}

	@Override
	public final <T extends PersistentProduct> T get(Class<T> clazz, ProductIdentifier productIdentifier)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(productIdentifier, "productIdentifier");
		EntityManager em = emf.createEntityManager();
		return em.find(clazz, productIdentifier);
	}

	@Override
	public final <T extends PersistentProduct> Iterable<T> find(Class<T> clazz)
	{
		Objects.requireNonNull(clazz, "clazz");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends PersistentProduct> Iterable<T> findByName(Class<T> clazz, String name)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(name, "name");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);
		cq.where(cb.like(entry.get(PersistentProduct_.name), name));
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	@Override
	public final <T extends PersistentProduct> Iterable<T> findByCategory(Class<T> clazz, String category)
	{
		Objects.requireNonNull(clazz, "clazz");
		Objects.requireNonNull(category, "category");

		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		Root<T> entry = cq.from(clazz);

		Predicate p2 = cb.isMember(category, entry.<Set<String>> get("categories"));

		// Overload Resolution fail?
		// Predicate p2 = cb.isMember(category,
		// entry.get(PersistentProductType_.categories));
		// PluralAttribute<PersistentProductType, Set<String>, String>
		// collection = PersistentProductType_.categories;
		// Expression<Set<String>> ex = entry.get(collection);

		cq.where(p2);
		TypedQuery<T> tq = em.createQuery(cq);

		return Iterables.of(tq.getResultList());
	}

	/**
	 * Updates and persists an existing {@link PersistentProduct} to the PersistentCatalog and the Database
	 * @param product the {@link PersistentProduct} to be updated
	 * @throws ArgumentNullException if product is null
	 */
	public final void update(PersistentProduct product)
	{
		Objects.requireNonNull(product, "product");
		EntityManager em = emf.createEntityManager();
		em.merge(product);
		beginCommit(em);
	}

	private final void beginCommit(EntityManager entityManager)
	{
		entityManager.getTransaction().begin();
		entityManager.getTransaction().commit();
	}
}

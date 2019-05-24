/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.xml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * Default implementation of the {@link BeanDefinitionDocumentReader} interface that
 * reads bean definitions according to the "spring-beans" DTD and XSD format
 * (Spring's default XML bean definition format).
 *
 * <p>The structure, elements, and attribute names of the required XML document
 * are hard-coded in this class. (Of course a transform could be run if necessary
 * to produce this format). {@code <beans>} does not need to be the root
 * element of the XML document: this class will parse all bean definition elements
 * in the XML file, regardless of the actual root element.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Erik Wiersma
 * @since 18.12.2003
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

	public static final String BEAN_ELEMENT = BeanDefinitionParserDelegate.BEAN_ELEMENT;

	public static final String NESTED_BEANS_ELEMENT = "beans";

	public static final String ALIAS_ELEMENT = "alias";

	public static final String NAME_ATTRIBUTE = "name";

	public static final String ALIAS_ATTRIBUTE = "alias";

	public static final String IMPORT_ELEMENT = "import";

	public static final String RESOURCE_ATTRIBUTE = "resource";

	public static final String PROFILE_ATTRIBUTE = "profile";


	protected final Log logger = LogFactory.getLog(getClass());

	@Nullable
	private XmlReaderContext readerContext;

	@Nullable
	private BeanDefinitionParserDelegate delegate;


	/**
	 * This implementation parses bean definitions according to the "spring-beans" XSD
	 * (or DTD, historically).
	 * <p>Opens a DOM Document; then initializes the default settings
	 * specified at the {@code <beans/>} level; then parses the contained bean definitions.
	 */
	// 根据spring的dtd或xds规则，对bean定义规则解析bean定义Document对象
	@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
		// 根据xml描述符
		this.readerContext = readerContext;
		// doc.getDocumentElement 获得document的根元素
		doRegisterBeanDefinitions(doc.getDocumentElement());
	}

	/**
	 * Return the descriptor for the XML resource that this parser works on.
	 */
	protected final XmlReaderContext getReaderContext() {
		Assert.state(this.readerContext != null, "No XmlReaderContext available");
		return this.readerContext;
	}

	/**
	 * Invoke the {@link org.springframework.beans.factory.parsing.SourceExtractor}
	 * to pull the source metadata from the supplied {@link Element}.
	 */
	@Nullable
	protected Object extractSource(Element ele) {
		return getReaderContext().extractSource(ele);
	}


	/**
	 * Register each bean definition within the given root {@code <beans/>} element.
	 */
	@SuppressWarnings("deprecation")  // for Environment.acceptsProfiles(String...)
	protected void doRegisterBeanDefinitions(Element root) {
		// Any nested <beans> elements will cause recursion in this method. In
		// order to propagate and preserve <beans> default-* attributes correctly,
		// keep track of the current (parent) delegate, which may be null. Create
		// the new (child) delegate with a reference to the parent for fallback purposes,
		// then ultimately reset this.delegate back to its original (parent) reference.
		// this behavior emulates a stack of delegates without actually necessitating one.

		// 一些嵌套的<beans>元素可能引起递归。为了正确的传播并保留<beans> default-*属性，请跟踪当前(父)委派，可能为空。
		// 利用父引用创建新的(子)委派为了能够回退，最终再重设委派给原始(父)引用。
		// 这个行为模拟了一个委派栈，而实际上并不需要一个代理

		//具体的解析过程由BeanDefinitionParserDelegate实现
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createDelegate(getReaderContext(), root, parent);

		if (this.delegate.isDefaultNamespace(root)) {
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (StringUtils.hasText(profileSpec)) {
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				// We cannot use Profiles.of(...) since profile expressions are not supported
				// in XML config. See SPR-12458 for details.
				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
					if (logger.isDebugEnabled()) {
						logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
								"] not matching: " + getReaderContext().getResource());
					}
					return;
				}
			}
		}

		// 解析之前进行自定义的解析，增强扩展性
		preProcessXml(root);
		// 从document的根元素开始进行bean定义的document对象
		parseBeanDefinitions(root, this.delegate);
		// 解析之后还有自定义的解析
		postProcessXml(root);

		this.delegate = parent;
	}

	// 创建BeanDefinitionParserDelegate，用于真正的解析过程
	protected BeanDefinitionParserDelegate createDelegate(
			XmlReaderContext readerContext, Element root, @Nullable BeanDefinitionParserDelegate parentDelegate) {

		BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
		// BeanDefinitionParserDelegate初始化document根元素
		delegate.initDefaults(root, parentDelegate);
		return delegate;
	}

	/**
	 * Parse the elements at the root level in the document:
	 * "import", "alias", "bean".
	 * @param root the DOM root element of the document
	 */
	// 使用 Spring 的 Bean 规则从 Document 的根元素开始进行 Bean 定义的 Document 对象
	protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
		if (delegate.isDefaultNamespace(root)) {
			//获取 Bean 定义的 Document 对象根元素的所有子节点
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				//获得 Document 节点是 XML 元素节点
				if (node instanceof Element) {
					Element ele = (Element) node;
					//Bean 定义的 Document 的元素节点使用的是 Spring 默认的 XML 命名空间
					if (delegate.isDefaultNamespace(ele)) {
						//使用 Spring 的 Bean 规则解析元素节点
						parseDefaultElement(ele, delegate);
					}
					else {
						//没有使用 Spring 默认的 XML 命名空间，则使用用户自定义的解析规则解析元素节点
						delegate.parseCustomElement(ele);
					}
				}
			}
		}
		else {
			//Document 的根节点没有使用 Spring 默认的命名空间，则使用用户自定义的解析规则解析 Document 根节点
			delegate.parseCustomElement(root);
		}
	}

	//使用 Spring 的 Bean 规则解析 Document 元素节点
	private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
		//如果元素节点是<Import>导入元素，进行导入解析
		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
			importBeanDefinitionResource(ele);
		}
		//如果元素节点是<Alias>别名元素，进行别名解析
		else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
			processAliasRegistration(ele);
		}
		//元素节点既不是导入元素，也不是别名元素，即普通的<Bean>元素，
		//按照 Spring 的 Bean 规则解析元素
		else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
			processBeanDefinition(ele, delegate);
		}
		else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
			// recurse
			doRegisterBeanDefinitions(ele);
		}
	}

	/**
	 * Parse an "import" element and load the bean definitions
	 * from the given resource into the bean factory.
	 *
	 * 1、获取resource属性所表示的路径
	 * 2、解析路径中的系统属性，格式如。。。
	 * 3、判断location是绝对路径还是相对路径
	 * 4、如果是绝对路径，则递归bean的解析过程，进行一次解析
	 * 5、如果是相对路径，则计算出绝对路径进行解析
	 * 6、通知监听器，解析完成
	 */
	//解析<Import>导入元素，从给定的导入路径加载 Bean 配置资源到 Spring IOC 容器中
	protected void importBeanDefinitionResource(Element ele) {
		//获取给定的导入元素的 location 属性
		String location = ele.getAttribute(RESOURCE_ATTRIBUTE);
		//如果导入元素的 location 属性值为空，则没有导入任何资源，直接返回
		if (!StringUtils.hasText(location)) {
			getReaderContext().error("Resource location must not be empty", ele);
			return;
		}

		// Resolve system properties: e.g. "${user.dir}"
		// 解析系统属性，格式为 "${user.dir}"
		// 使用系统变量值解析 location 属性值
		location = getReaderContext().getEnvironment().resolveRequiredPlaceholders(location);

		Set<Resource> actualResources = new LinkedHashSet<>(4);

		// Discover whether the location is an absolute or relative URI
		//标识给定的导入元素的 location 是否是绝对路径
		boolean absoluteLocation = false;
		try {
			absoluteLocation = ResourcePatternUtils.isUrl(location) || ResourceUtils.toURI(location).isAbsolute();
		}
		catch (URISyntaxException ex) {
			// cannot convert to an URI, considering the location relative
			// unless it is the well-known Spring prefix "classpath*:"
		}

		// Absolute or relative?
		// 给定的导入元素的 location 是绝对路径
		if (absoluteLocation) {
			try {
				//使用资源读入器加载给定路径的 Bean 配置资源
				int importCount = getReaderContext().getReader().loadBeanDefinitions(location, actualResources);
				if (logger.isTraceEnabled()) {
					logger.trace("Imported " + importCount + " bean definitions from URL location [" + location + "]");
				}
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error(
						"Failed to import bean definitions from URL location [" + location + "]", ele, ex);
			}
		}
		else {
			// No URL -> considering resource location as relative to the current file.
			// 如果是相对地址则根据相对地址计算出绝对地址
			try {
				int importCount;
				// 将给定导入元素的 location 封装为相对路径资源
				// Resource存在多个实现子类，而每个resource的createRelative实现方式都不一样，所以这里先试用子类的方式先尝试
				Resource relativeResource = getReaderContext().getResource().createRelative(location);
				//封装的相对路径资源存在
				if (relativeResource.exists()) {
					//使用资源读入器加载 Bean 配置资源
					importCount = getReaderContext().getReader().loadBeanDefinitions(relativeResource);
					actualResources.add(relativeResource);
				}
				// 如果解析不成功，则使用默认的解析器ResourcePatternResolver进行解析
				else {
					//获取 Spring IOC 容器资源读入器的基本路径
					String baseLocation = getReaderContext().getResource().getURL().toString();
					//根据 Spring IOC 容器资源读入器的基本路径加载给定导入路径的资源
					importCount = getReaderContext().getReader().loadBeanDefinitions(
							StringUtils.applyRelativePath(baseLocation, location), actualResources);
				}
				if (logger.isTraceEnabled()) {
					logger.trace("Imported " + importCount + " bean definitions from relative location [" + location + "]");
				}
			}
			catch (IOException ex) {
				getReaderContext().error("Failed to resolve current resource location", ele, ex);
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error(
						"Failed to import bean definitions from relative location [" + location + "]", ele, ex);
			}
		}
		Resource[] actResArray = actualResources.toArray(new Resource[0]);
		//在解析完<Import>元素之后，发送容器导入其他资源处理完成事件
		getReaderContext().fireImportProcessed(location, actResArray, extractSource(ele));
	}

	/**
	 * Process the given alias element, registering the alias with the registry.
	 */
	//解析<Alias>别名元素，为 Bean 向 Spring IOC 容器注册别名
	protected void processAliasRegistration(Element ele) {
		//获取<Alias>别名元素中 name 的属性值
		String name = ele.getAttribute(NAME_ATTRIBUTE);
		//获取<Alias>别名元素中 alias 的属性值
		String alias = ele.getAttribute(ALIAS_ATTRIBUTE);
		boolean valid = true;
		//<alias>别名元素的 name 属性值为空
		if (!StringUtils.hasText(name)) {
			getReaderContext().error("Name must not be empty", ele);
			valid = false;
		}
		//<alias>别名元素的 alias 属性值为空
		if (!StringUtils.hasText(alias)) {
			getReaderContext().error("Alias must not be empty", ele);
			valid = false;
		}
		if (valid) {
			try {
				//向容器的资源读入器注册别名
				getReaderContext().getRegistry().registerAlias(name, alias);
			}
			catch (Exception ex) {
				getReaderContext().error("Failed to register alias '" + alias +
						"' for bean with name '" + name + "'", ele, ex);
			}
			//在解析完<Alias>元素之后，发送容器别名处理完成事件
			getReaderContext().fireAliasRegistered(name, alias, extractSource(ele));
		}
	}

	/**
	 * Process the given bean element, parsing the bean definition
	 * and registering it with the registry.
	 *
	 * 1、首先委托BeanDefinitionDelegate的parseBeanDefinitionElement方法进行元素解析，经过该方法，bdHolder实例已经包含了配置文件中配置的各种属性，
	 * 如：class、id、name、alias等
	 * 2、当放回的bdHolder不为空的情况下，若存在默认标签子节点下再有自定义属性，还需要再次对自定义标签进行解析
	 * 3、解析完成后需要对解析后的bdHolder进行注册，注册过程委托给了BeanDefinitionReaderUtils的registerBeanDefinition方法
	 * 4、最后发出响应事件，通知相关监听器，这个bean已经加载完成
	 */
	//解析 Bean 配置资源 Document 对象的普通元素
	protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
		// BeanDefinitionHolder 是对 BeanDefinition 的封装，即 Bean 定义的封装类
		// 对 Document 对象中<Bean>元素的解析由 BeanDefinitionParserDelegate 实现
		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
		if (bdHolder != null) {
			// 如果需要的话对beanDefinition进行装饰。 也就是说在默认标签里加了自定义标签，那么这句代码就会起作用
			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
			try {
				// Register the final decorated instance.
				// 向 Spring IOC 容器注册解析得到的 Bean 定义，这是 Bean 定义向 IOC 容器注册的入口
				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
			}
			catch (BeanDefinitionStoreException ex) {
				getReaderContext().error("Failed to register bean definition with name '" +
						bdHolder.getBeanName() + "'", ele, ex);
			}
			// Send registration event.
			//在完成向 Spring IOC 容器注册解析得到的 Bean 定义之后，发送注册事件
			// 这里监听是对外扩展的，暂时spring中还没有任何处理逻辑。需要自定义开发，这个可以玩玩
			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
		}
	}


	/**
	 * Allow the XML to be extensible by processing any custom element types first,
	 * before we start to process the bean definitions. This method is a natural
	 * extension point for any other custom pre-processing of the XML.
	 * <p>The default implementation is empty. Subclasses can override this method to
	 * convert custom elements into standard Spring bean definitions, for example.
	 * Implementors have access to the parser's bean definition reader and the
	 * underlying XML resource, through the corresponding accessors.
	 * @see #getReaderContext()
	 */
	protected void preProcessXml(Element root) {
	}

	/**
	 * Allow the XML to be extensible by processing any custom element types last,
	 * after we finished processing the bean definitions. This method is a natural
	 * extension point for any other custom post-processing of the XML.
	 * <p>The default implementation is empty. Subclasses can override this method to
	 * convert custom elements into standard Spring bean definitions, for example.
	 * Implementors have access to the parser's bean definition reader and the
	 * underlying XML resource, through the corresponding accessors.
	 * @see #getReaderContext()
	 */
	protected void postProcessXml(Element root) {
	}

}

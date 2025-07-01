import { NavLink } from 'react-router-dom';
import styles from './Sidebar.module.css';

const Sidebar = () => (
    <div className={styles.sidebar}>
        <div className={styles.header}>LobeHub</div>
        <nav className={styles.nav}>
            <NavLink to="/" className={({ isActive }) => isActive ? `${styles.navLink} ${styles.active}` : styles.navLink}>
                随便聊天
            </NavLink>
            <NavLink to="/market" className={({ isActive }) => isActive ? `${styles.navLink} ${styles.active}` : styles.navLink}>
                提示词市场
            </NavLink>
            {/* 这里后续可以动态渲染历史会话列表 */}
        </nav>
    </div>
);

export default Sidebar;